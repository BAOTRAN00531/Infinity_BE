package com.example.infinityweb_be.service.skill;

import com.example.infinityweb_be.domain.dto.skills.SpeakingScoreDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SpeakingService {

    private static final JaroWinklerSimilarity JWS = new JaroWinklerSimilarity();
    private final JdbcTemplate jdbc;
    private final ObjectMapper om = new ObjectMapper();

    public SpeakingService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    // ffmpeg (đường dẫn thực thi). Nếu đã add vào PATH thì để mặc định là "ffmpeg"
    @Value("${ffmpeg.path:ffmpeg}")
    private String ffmpeg;

    // credentials cho Google STT (nếu muốn chỉ định file riêng). Có thể bỏ trống để dùng GOOGLE_APPLICATION_CREDENTIALS
    @Value("${google.stt.credentials-file:}")
    private String sttCredFile;

    // fallback: dùng chung file TTS nếu không set riêng STT
    @Value("${google.tts.credentials-file:}")
    private String ttsCredFile;

    /** API chính */
    public SpeakingScoreDto grade(MultipartFile audio,
                                  String audioBase64,
                                  String lang,
                                  String target,
                                  Long questionId) throws Exception {

        // 1) lấy bytes audio
        byte[] inputBytes = extractAudioBytes(audio, audioBase64);
        if (inputBytes == null) throw new IllegalArgumentException("Missing audio");

        // 2) convert -> wav 16k mono
        byte[] wav16k = toWav16kMono(inputBytes);

        // 3) Google STT
        String gLang = (lang == null || lang.isBlank()) ? "en-US" : lang;
        SttResult stt = sttWithGoogle(wav16k, gLang);

        // 4) lấy targets & rubric từ DB (nếu có questionId)
        List<String> targets = new ArrayList<>();
        RubricConfig cfg = new RubricConfig();
        String rubricLang = "en";

        if (questionId != null) {
            Map<String, Object> row = jdbc.queryForMap(
                    "SELECT q.correct_text, q.alt_answers_json, COALESCE(r.lang,'en') AS lang, r.config " +
                            "FROM dbo.Questions q LEFT JOIN dbo.writing_rubric r ON r.id=q.writing_rubric_id " +
                            "WHERE q.id = ?", questionId);

            String correct = s(row.get("correct_text"));
            if (!correct.isBlank()) targets.add(correct);
            targets.addAll(parseAltListSafe(s(row.get("alt_answers_json"))));

            RubricConfig dbCfg = parseConfigSafe(s(row.get("config")));
            cfg = (dbCfg != null ? dbCfg : cfg);
            rubricLang = s(row.get("lang"));
            if (cfg.altTargets != null) targets.addAll(cfg.altTargets);
        } else {
            if (target == null || target.isBlank())
                throw new IllegalArgumentException("Missing target or questionId");
            targets.add(target);
        }

        // 5) normalize transcript & targets
        String tx = normalize(stt.transcript);
        List<String> normTargets = targets.stream().map(SpeakingService::normalize).toList();

        // 6) hard-zero (quét cả top-1 lẫn mọi alternative)
        String hz = hardZero(tx);
        if (hz == null && stt.altTranscripts != null && !stt.altTranscripts.isEmpty()) {
            String altsJoined = normalize(String.join(" ", stt.altTranscripts));
            hz = hardZero(altsJoined);
        }
        if (hz != null) {
            return SpeakingScoreDto.builder()
                    .scoreTotal(0)
                    .pronScore(Math.round(stt.avgConfidence * 1000.0) / 10.0) // tham khảo
                    .completeness(0)
                    .fluency(0)
                    .tips(new String[]{hz})
                    .build();
        }

        // 7) tính độ khớp nội dung theo nhiều target (Jaro–Winkler + token + 1-WER)
        double bestWerScore = 0; // (1 - WER)
        double jwW  = (cfg.jwWeight    != null) ? cfg.jwWeight    : 0.3;
        double tokW = (cfg.tokenWeight != null) ? cfg.tokenWeight : 0.4;
        double werW = (cfg.semWeight   != null) ? cfg.semWeight   : 0.3;

        double bestBase = 0;
        for (String t : normTargets) {
            double jw = jaroWinkler(tx, t);
            double tok = tokenSimilarity(tx, t);
            double werScore = (1.0 - werFraction(tx, t)); // 1 - WER
            double base = jwW * jw + tokW * tok + werW * werScore;
            if (base > bestBase) {
                bestBase = base;
                bestWerScore = werScore;
            }
        }

        // 8) đặc trưng nói: pause / tốc độ / filler / confidence
        int longPauses = countLongPauses(stt, 700);
        int wpm = calcWpm(stt);
        int fillers = countFillers(tx, rubricLang);
        boolean lowConf = (stt.avgConfidence < 0.70);

        // 9) ba điểm thành phần
        double pron = stt.avgConfidence * 100.0;
        pron -= Math.min(10, fillers * 2);      // phạt nhẹ vì filler
        pron -= Math.min(10, longPauses * 2);   // phạt nhẹ vì ngắt dài
        if (lowConf) pron -= 5;
        pron = clampDouble(pron);

        double completeness = clampDouble(bestWerScore * 100.0);

        double fluency = 100.0;
        if (wpm < 70)  fluency -= Math.min(30, 70 - wpm);   // quá chậm
        if (wpm > 180) fluency -= Math.min(30, wpm - 180);  // quá nhanh
        fluency -= Math.min(40, longPauses * 10);           // mỗi ngắt dài -10 (trần 40)
        fluency = clampDouble(fluency);

        // 10) tổng điểm
        double total = 0.4 * pron + 0.4 * completeness + 0.2 * fluency;
        total = clampDouble(total);

        // 11) tips
        List<String> tips = new ArrayList<>();
        if (completeness < 80) tips.add("Cố gắng nói đủ ý/chìa khóa như đáp án mục tiêu.");
        if (longPauses > 0) tips.add("Giảm các khoảng ngắt >700ms.");
        if (fillers > 0) tips.add("Hạn chế filler (uh/um).");
        if (wpm < 90 || wpm > 170) tips.add("Giữ tốc độ ~110–160 WPM.");
        if (tips.isEmpty()) tips.add("Tốt! Duy trì phát âm rõ và tốc độ ổn định.");

        return SpeakingScoreDto.builder()
                .scoreTotal(round1(total))
                .pronScore(round1(pron))
                .completeness(round1(completeness))
                .fluency(round1(fluency))
                .tips(tips.toArray(String[]::new))
                .build();
    }

    /* ===================== Audio I/O ===================== */

    private byte[] extractAudioBytes(MultipartFile audio, String audioBase64) throws IOException {
        if (audio != null && !audio.isEmpty()) return audio.getBytes();
        if (audioBase64 != null && !audioBase64.isBlank()) {
            int i = audioBase64.indexOf(";base64,");
            String b64 = (i > 0) ? audioBase64.substring(i + 8) : audioBase64;
            return Base64.getDecoder().decode(b64);
        }
        return null;
    }

    private byte[] toWav16kMono(byte[] input) throws Exception {
        Path in = Files.createTempFile("in_", ".bin");
        Path out = Files.createTempFile("out_", ".wav");
        Files.write(in, input);

        Process p = new ProcessBuilder(
                ffmpeg, "-y", "-i", in.toString(),
                "-ac", "1", "-ar", "16000", "-f", "wav", out.toString()
        ).redirectErrorStream(true).start();
        p.waitFor();

        byte[] wav = Files.readAllBytes(out);
        Files.deleteIfExists(in);
        Files.deleteIfExists(out);
        return wav;
    }

    /* ===================== Google STT ===================== */

    private SpeechClient buildSpeechClient() throws IOException {
        String path = (sttCredFile != null && !sttCredFile.isBlank()) ? sttCredFile
                : (ttsCredFile != null && !ttsCredFile.isBlank()) ? ttsCredFile : null;

        if (path != null) {
            try (var in = Files.newInputStream(Path.of(path))) {
                GoogleCredentials cred = GoogleCredentials.fromStream(in)
                        .createScoped("https://www.googleapis.com/auth/cloud-platform");
                SpeechSettings settings = SpeechSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(cred))
                        .build();
                return SpeechClient.create(settings);
            }
        }
        return SpeechClient.create(); // fallback: GOOGLE_APPLICATION_CREDENTIALS
    }

    private SttResult sttWithGoogle(byte[] wav16k, String lang) throws Exception {
        try (SpeechClient client = buildSpeechClient()) {

            // ====== PHRASE HINTS + BOOST để hạn chế tự sửa ======
            RecognitionConfig.Builder cfgBuilder = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode(lang)
                    .setEnableWordTimeOffsets(true)
                    // LẤY NHIỀU PHƯƠNG ÁN -> ta mới kiểm tra được alt
                    .setMaxAlternatives(5)                 // <— thêm dòng này
                    .setUseEnhanced(true);                 // optional: dùng model tăng cường

            if (lang != null && lang.toLowerCase(Locale.ROOT).startsWith("en")) {
                SpeechContext hints = SpeechContext.newBuilder()
                        // các cụm "sai" mà ta cố giữ nguyên để bắt lỗi ngữ pháp
                        .addPhrases("i has")
                        .addPhrases("he have")
                        .addPhrases("she have")
                        .addPhrases("we has")
                        .addPhrases("they has")
                        // đẩy độ ưu tiên cho hints để giảm tự sửa
                        .setBoost(20.0f)                   // <— rất quan trọng
                        .build();
                cfgBuilder.addSpeechContexts(hints);
                // bạn có thể thử các model khác:
                // cfgBuilder.setModel("default"); // hoặc "phone_call", "video", ...
            }

            RecognitionConfig cfg = cfgBuilder.build();

            RecognitionAudio aud = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(wav16k))
                    .build();

            RecognizeResponse resp = client.recognize(cfg, aud);

            StringBuilder transcript = new StringBuilder();
            double confSum = 0; int confCnt = 0;
            List<Word> words = new ArrayList<>();
            List<String> altTexts = new ArrayList<>();

            for (SpeechRecognitionResult r : resp.getResultsList()) {
                if (r.getAlternativesCount() == 0) continue;

                // TOP-1
                SpeechRecognitionAlternative alt0 = r.getAlternatives(0);
                transcript.append(alt0.getTranscript()).append(" ");
                float conf = alt0.getConfidence();
                if (conf > 0f) { confSum += conf; confCnt++; }

                // GOM TẤT CẢ ALTERNATIVES (nhờ setMaxAlternatives ở trên)
                for (SpeechRecognitionAlternative a : r.getAlternativesList()) {
                    String t = a.getTranscript();
                    if (t != null && !t.isBlank()) altTexts.add(t);
                }

                for (WordInfo w : alt0.getWordsList()) {
                    long start = w.getStartTime().getSeconds()*1000L + w.getStartTime().getNanos()/1_000_000;
                    long end   = w.getEndTime().getSeconds()*1000L + w.getEndTime().getNanos()/1_000_000;
                    words.add(new Word(w.getWord(), start, end));
                }
            }

            double avg = (confCnt == 0) ? 0.9 : (confSum / confCnt);
            return new SttResult(transcript.toString().trim(), avg, words, altTexts);
        }
    }

    /* ===================== Scoring helpers ===================== */

    private static String normalize(String s) {
        if (s == null) return "";
        String t = s.toLowerCase(Locale.ROOT).trim();
        t = t.replace('\u2019','\'').replace('\u2018','\'').replace('\u02BC','\'').replace('\u2032','\'');
        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}+","");   // bỏ dấu
        t = t.replaceAll("[^\\p{L}\\p{Nd}\\s']"," ");                                  // giữ chữ/số/khoảng trắng/ '
        return t.replaceAll("\\s+"," ").trim();
    }

    private static double jaroWinkler(String a, String b) {
        Double d = JWS.apply(a, b);
        return d == null ? 0.0 : d;
    }

    private static double tokenSimilarity(String a, String b) {
        String[] ta = a.split("\\s+");
        String[] tb = b.split("\\s+");
        if (ta.length == 0 || tb.length == 0) return 0;

        double sumA = 0;
        for (String wa : ta) {
            double best = 0;
            for (String wb : tb) {
                Double s = JWS.apply(wa, wb);
                if (s != null && s > best) best = s;
            }
            sumA += best;
        }
        double sumB = 0;
        for (String wb : tb) {
            double best = 0;
            for (String wa : ta) {
                Double s = JWS.apply(wb, wa);
                if (s != null && s > best) best = s;
            }
            sumB += best;
        }
        return (sumA/ta.length + sumB/tb.length) / 2.0;
    }

    // WER = editDistance(ref, hyp) / refWords
    private static double werFraction(String hyp, String ref) {
        String[] H = hyp.split("\\s+"), R = ref.split("\\s+");
        int n = R.length, m = H.length;
        int[][] dp = new int[n+1][m+1];
        for (int i=0;i<=n;i++) dp[i][0]=i;
        for (int j=0;j<=m;j++) dp[0][j]=j;
        for (int i=1;i<=n;i++) {
            for (int j=1;j<=m;j++) {
                int cost = R[i-1].equals(H[j-1]) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i-1][j]+1, dp[i][j-1]+1), dp[i-1][j-1]+cost);
            }
        }
        return n==0 ? (m==0?0:1.0) : (double) dp[n][m] / n;
    }

    private static int countLongPauses(SttResult r, long thrMs) {
        if (r.words.size() < 2) return 0;
        int c = 0;
        for (int i=1;i<r.words.size();i++) {
            long gap = r.words.get(i).start - r.words.get(i-1).end;
            if (gap >= thrMs) c++;
        }
        return c;
    }

    private static int calcWpm(SttResult r) {
        if (r.words.isEmpty()) return 0;
        long dur = r.words.get(r.words.size()-1).end - r.words.get(0).start;
        if (dur <= 0) return 0;
        double minutes = dur / 60000.0;
        return (int) Math.round(r.words.size() / minutes);
    }

    private static int countFillers(String tx, String lang) {
        return Pattern.compile("\\b(uh|um|erm|ah|uhm|à|ừm|ờ)\\b").matcher(tx).results().toList().size();
    }

    /** Hard-zero lỗi nặng – EN: have/has, do/does (trừ “has got / ’ve got”). */
    private static String hardZero(String tx) {
        if (tx.matches(".*\\b(i|you|we|they)\\s+has\\b.*"))
            return "Sai chia động từ: với I/you/we/they dùng “have”, không dùng “has”.";
        if (tx.matches(".*\\b(he|she|it)\\s+have\\b(?!\\s+got\\b).*"))
            return "Sai chia động từ: với he/she/it dùng “has” (trừ cấu trúc “has got”).";
        if (tx.matches(".*\\b(he|she|it)\\s+do(?:n'?t)?\\b.*"))
            return "Hiện tại đơn: với he/she/it dùng “does/doesn’t”.";
        if (tx.matches(".*\\b(i|you|we|they)\\s+does(?:n'?t)?\\b.*"))
            return "Hiện tại đơn: với I/you/we/they dùng “do/don’t”.";
        return null;
    }

    private RubricConfig parseConfigSafe(String json) {
        if (json == null || json.isBlank()) return new RubricConfig();
        try { return om.readValue(json, RubricConfig.class); }
        catch (Exception e) { return new RubricConfig(); }
    }

    private List<String> parseAltListSafe(String json) {
        if (json == null || json.isBlank()) return List.of();
        try { return om.readValue(json, new TypeReference<List<String>>(){}); }
        catch (Exception e) { return List.of(); }
    }

    private static String s(Object o) { return o == null ? "" : String.valueOf(o); }
    private static double clampDouble(double v){ return Math.max(0, Math.min(100, v)); }
    private static double round1(double v){ return Math.round(v*10.0)/10.0; }

    /* ====== types ====== */
    private static class RubricConfig {
        public Double jwWeight = 0.3;
        public Double tokenWeight = 0.4;
        public Double semWeight = 0.3;
        public Integer passThreshold = 70;
        public Integer minLen = 3;
        public List<String> mustContainAny = new ArrayList<>();
        public List<String> mustContainAll = new ArrayList<>();
        public List<String> forbid = new ArrayList<>();
        public List<String> regexMust = new ArrayList<>();
        public List<String> regexForbid = new ArrayList<>();
        public List<PenaltyRule> penalties = new ArrayList<>();
        public List<HardZeroRule> hardZeroRules = new ArrayList<>();
        public List<String> altTargets = new ArrayList<>();
    }
    private static class PenaltyRule { public String pattern; public Integer value; }
    private static class HardZeroRule { public String pattern; public String message; }

    private static class SttResult {
        final String transcript;
        final double avgConfidence;
        final List<Word> words;
        final List<String> altTranscripts;   // <— NEW

        SttResult(String t, double c, List<Word> w, List<String> alts){
            this.transcript=t; this.avgConfidence=c; this.words=w; this.altTranscripts = alts;
        }
    }
    private static class Word { final String text; final long start,end; Word(String t,long s,long e){text=t;start=s;end=e;} }

}
