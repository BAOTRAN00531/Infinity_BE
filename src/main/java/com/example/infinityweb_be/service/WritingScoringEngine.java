package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.RubricConfig;
import com.example.infinityweb_be.domain.response.GradeOpenResponse;
import com.example.infinityweb_be.service.skill.LanguageToolService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.languagetool.rules.RuleMatch;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class WritingScoringEngine {

    private final LanguageToolService lt;     // Grammar/Spelling AI (LanguageTool)
    private final JdbcTemplate jdbc;          // Đọc target/rubric từ DB
    private final ObjectMapper om;            // Parse JSON rubric

    private static final JaroWinklerSimilarity JWS = new JaroWinklerSimilarity();
    private static final boolean DEBUG = false; // bật true nếu muốn in log debug

    /* ========================= PUBLIC API ========================= */

    /** Chấm theo questionId: tự lấy target/alt + rubric từ DB. */
    public GradeOpenResponse scoreByQuestion(Long qid, String learnerText) {
        Map<String, Object> row;
        try {
            row = jdbc.queryForMap(
                    "SELECT q.correct_text, q.alt_answers_json, COALESCE(r.lang,'en') lang, " +
                            "COALESCE(r.type,'composite') type, r.config " +
                            "FROM dbo.Questions q " +
                            "LEFT JOIN dbo.writing_rubric r ON r.id = q.writing_rubric_id " +
                            "WHERE q.id = ?",
                    qid
            );
        } catch (Exception e) {
            return new GradeOpenResponse(0, "Không tìm thấy câu hỏi #" + qid);
        }

        String correct = safeStr(row.get("correct_text"));
        String altJson = safeStr(row.get("alt_answers_json"));
        String lang    = safeStr(row.get("lang"));
        RubricConfig cfg = parseConfigSafe(safeStr(row.get("config")));

        List<String> targets = new ArrayList<>();
        if (!correct.isBlank()) targets.add(correct);
        targets.addAll(parseAltListSafe(altJson));
        // altTargets khai trong rubric (tuỳ chọn)
        if (cfg.getAltTargets() != null) targets.addAll(cfg.getAltTargets());

        return scoreWithConfig(learnerText, targets, lang, cfg);
    }

    /** Chấm theo text + targets + rubric truyền vào (dùng cho test nhanh). */
    public GradeOpenResponse scoreWithConfig(String learnerText,
                                             List<String> targetsRaw,
                                             String lang,
                                             RubricConfig cfg) {
        // 0) Chuẩn hoá đầu vào
        String a = norm(learnerText);
        String hardZeroMsg = hardZeroHaveHas(a);
        if (hardZeroMsg != null) {
            return new GradeOpenResponse(0, hardZeroMsg);
        }
        String hz = matchHardZero(a, cfg);
        if (hz != null) return new GradeOpenResponse(0, hz);
        String hzDoDoes = hardZeroDoDoes(a);
        if (hzDoDoes != null) return new GradeOpenResponse(0, hzDoDoes);
        List<String> targets = (targetsRaw == null || targetsRaw.isEmpty())
                ? List.of("")
                : targetsRaw.stream().map(WritingScoringEngine::norm).toList();

// (nếu bạn đã có hardZeroHaveHas thì gọi nó ở đây luôn)
        String hzHaveHas = hardZeroHaveHas(a);
        if (hzHaveHas != null) return new GradeOpenResponse(0, hzHaveHas);
        // 1) Base similarity (max trên các target/alt)
        double base = 0.0;
        for (String t : targets) {
            double sToken = tokenSim(a, t);
            double sJW    = jw(a, t);
            double sSem   = 0.0; // chỗ gắn embeddings nếu có microservice
            double mix    = cfg.getTokenWeight() * sToken
                    + cfg.getJwWeight()    * sJW
                    + cfg.getSemWeight()   * sSem;
            base = Math.max(base, mix);
        }
        base = Math.max(0, Math.min(1.0, base)); // clamp 0..1

        // 2) Penalty từ LanguageTool (AI grammar/spelling)
        Pair<Integer,String> ltRes = lt.penaltyAndFeedback(learnerText, lang);
        int penLt = safeInt(ltRes.getLeft());
        String tip = ltRes.getRight();

        // 2b) Bonus phạt riêng cho agreement nếu phát hiện (đọc trực tiếp LT)
        int penAgreement = 0;
        try {
            List<RuleMatch> matches = lt.check(learnerText, lang);
            for (RuleMatch m : matches) {
                String idU  = (m.getRule().getId() == null ? "" : m.getRule().getId()).toUpperCase(Locale.ROOT);
                String msgU = (m.getMessage() == null ? "" : m.getMessage()).toUpperCase(Locale.ROOT);
                if (idU.contains("AGREEMENT") || msgU.contains("AGREEMENT")) {
                    penAgreement += 12; // phạt thêm mạnh lỗi chia động từ
                }
            }
        } catch (Exception ignored) {}

        // 3) Penalty từ rubric (regex/must/forbid/penalties/hardPenalties)
        int penRx = 0;
        if (cfg.getRegexForbid() != null) {
            for (String r : cfg.getRegexForbid()) if (find(a, r)) penRx += 12; // tăng trừ nặng
        }
        if (cfg.getRegexMust() != null) {
            for (String r : cfg.getRegexMust())  if (!find(a, r)) penRx += 10;
        }
        if (cfg.getForbid() != null) {
            for (String f : cfg.getForbid()) if (find(a, "\\b" + Pattern.quote(f) + "\\b")) penRx += 8;
        }
        boolean anyOk = cfg.getMustContainAny() == null || cfg.getMustContainAny().isEmpty();
        if (!anyOk) {
            for (String k : cfg.getMustContainAny()) {
                if (find(a, "\\b" + Pattern.quote(k) + "\\b")) { anyOk = true; break; }
            }
        }
        if (!anyOk) penRx += 10;

        if (cfg.getPenalties() != null) {
            for (RubricConfig.PenaltyRule pr : cfg.getPenalties()) {
                if (find(a, pr.getPattern())) penRx += pr.getValue();
            }
        }

        // Hard penalties: đánh mạnh + option hạ trần điểm khi dính
        int hardMinus = 0; boolean hardHit = false;
        if (cfg.getHardPenalties() != null) {
            for (RubricConfig.PenaltyRule pr : cfg.getHardPenalties()) {
                if (find(a, pr.getPattern())) {
                    hardHit = true;
                    hardMinus = Math.max(hardMinus, pr.getValue());
                }
            }
        }

        // 4) Tính điểm cuối
        double scoreRaw = 100.0 * base - (penLt + penAgreement + penRx + hardMinus);
        if (hardHit) scoreRaw = Math.min(scoreRaw, 60); // nếu dính hard rule: trần 60
        int score = (int) round(scoreRaw);

        // 5) Feedback: ưu tiên tip từ LT; sau đó dựa theo rubric; cuối cùng là default
        String fb;
        if (tip != null && !tip.isBlank()) {
            fb = tip;
        } else if (!anyOk) {
            fb = "Bài yêu cầu có từ/cấu trúc: " + String.join(", ", cfg.getMustContainAny());
        } else if (hardHit) {
            fb = "Có lỗi nghiêm trọng với cấu trúc bắt buộc. Hãy sửa và thử lại.";
        } else {
            fb = (score >= cfg.getPassThreshold()) ? "Câu ổn. Rất tốt!" : "Ổn rồi. Có thể diễn đạt tự nhiên hơn.";
        }

        if (DEBUG) {
            System.out.println("DBG a        = " + a);
            System.out.println("DBG base     = " + base);
            System.out.println("DBG penLt    = " + penLt + " (agreement +" + penAgreement + ")");
            System.out.println("DBG penRx    = " + penRx + ", hardMinus=" + hardMinus + ", hardHit=" + hardHit);
            System.out.println("DBG scoreRaw = " + scoreRaw + " -> score=" + score);
        }

        return new GradeOpenResponse(score, fb);
    }

    /* ========================= UTILS ========================= */

    private static String safeStr(Object o) { return o == null ? "" : String.valueOf(o); }
    private static int safeInt(Integer i) { return i == null ? 0 : i; }

    private RubricConfig parseConfigSafe(String json) {
        if (json == null || json.isBlank()) return new RubricConfig();
        try {
            return om.readValue(json, RubricConfig.class);
        } catch (Exception e) {
            if (DEBUG) System.out.println("WARN rubric config parse failed, using default. " + e.getMessage());
            return new RubricConfig();
        }
    }

    private List<String> parseAltListSafe(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return om.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            if (DEBUG) System.out.println("WARN alt_answers_json parse failed, using []. " + e.getMessage());
            return List.of();
        }
    }

    private static String norm(String s) {
        if (s == null) return "";
        String t = s.toLowerCase(Locale.ROOT).trim();
        t = t
                .replace('\u2019', '\'')  // ’
                .replace('\u2018', '\'')  // ‘
                .replace('\u02BC', '\'')  // ʼ
                .replace('\u2032', '\''); // ′

        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        t = t.replaceAll("[^\\p{L}\\p{Nd}\\s']", " ");  // giữ chữ/số/khoảng trắng/‘
        return t.replaceAll("\\s+", " ").trim();
    }

    private static double jw(String a, String b) {
        Double d = JWS.apply(a, b); return d == null ? 0.0 : d;
    }

    /** Trung bình “mỗi từ khớp tốt nhất” 2 chiều (0..1). */
    private static double tokenSim(String a, String b) {
        String[] ta = a.split("\\s+"), tb = b.split("\\s+");
        if (ta.length == 0 || tb.length == 0) return 0;
        double sumA = 0;
        for (String wa : ta) {
            double best = 0;
            for (String wb : tb) best = Math.max(best, jw(wa, wb));
            sumA += best;
        }
        double sumB = 0;
        for (String wb : tb) {
            double best = 0;
            for (String wa : ta) best = Math.max(best, jw(wb, wa));
            sumB += best;
        }
        return (sumA / ta.length + sumB / tb.length) / 2.0;
    }

    private static boolean find(String text, String regex) {
        try {
            return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text).find();
        } catch (Exception e) {
            // regex lỗi cú pháp -> coi như không khớp
            return false;
        }
    }
    private static String matchHardZero(String a, RubricConfig cfg){
        if (cfg.getHardZeroRules() == null) return null;
        for (RubricConfig.HardZeroRule r : cfg.getHardZeroRules()) {
            if (r != null && r.getPattern() != null && find(a, r.getPattern()))
                return (r.getMessage()==null || r.getMessage().isBlank())
                        ? "Lỗi ngữ pháp nghiêm trọng." : r.getMessage();
        }
        return null;
    }
    /** Quy tắc 0 điểm cho have/has:
     *  - I/you/we/they + has  -> 0 điểm
     *  - he/she/it + have     -> 0 điểm (KHÔNG bắt khi là "have got ...")
     */
    private static String hardZeroHaveHas(String a) {
        // I/you/we/they + has
        if (java.util.regex.Pattern.compile("\\b(i|you|we|they)\\s+has\\b", java.util.regex.Pattern.CASE_INSENSITIVE)
                .matcher(a).find()) {
            return "Sai chia động từ: với I/you/we/they phải dùng “have”, không phải “has”.";
        }
        // he/she/it + have (tránh "have got")
        if (java.util.regex.Pattern.compile("\\b(he|she|it)\\s+have\\b(?!\\s+got\\b)", java.util.regex.Pattern.CASE_INSENSITIVE)
                .matcher(a).find()) {
            return "Sai chia động từ: với he/she/it phải dùng “has” (không dùng “have”).";
        }
        return null;
    }
    // He/she/it + do|don't  -> 0 điểm
// I/you/we/they + does|doesn't -> 0 điểm
    private static String hardZeroDoDoes(String a) {
        // Cho phép cả "don't" và "dont" -> n'?t
        if (java.util.regex.Pattern.compile("\\b(he|she|it)\\s+do(?:n'?t)?\\b").matcher(a).find()) {
            return "Hiện tại đơn: với he/she/it phải dùng “does/doesn’t”.";
        }
        if (java.util.regex.Pattern.compile("\\b(i|you|we|they)\\s+does(?:n'?t)?\\b").matcher(a).find()) {
            return "Hiện tại đơn: với I/you/we/they phải dùng “do/don’t”.";
        }
        return null;
    }


    /** Làm tròn và clamp 0..100. */
    private static double round(double v) {
        v = Math.round(v);
        if (v < 0) return 0;
        if (v > 100) return 100;
        return v;
    }
}
