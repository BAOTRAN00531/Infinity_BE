//package com.example.infinityweb_be.controller.skill;
//
//import com.example.infinityweb_be.domain.request.GradeOpenRequest;
//import com.example.infinityweb_be.domain.response.GradeOpenResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//import org.apache.commons.text.similarity.JaroWinklerSimilarity;
//import java.text.Normalizer;
//import java.util.Locale;
//@RestController
//@RequestMapping("/api/ai")
//@RequiredArgsConstructor
//public class WritingController {
//    @PostMapping("/grade-open")
//    public ResponseEntity<GradeOpenResponse> grade(@RequestBody GradeOpenRequest req){
//        String a = normalize(req.getText());
//        String b = normalize(req.getTarget());
//
//        double base = 0.6 * tokenSimilarity(a, b) + 0.4 * jaroWinkler(a, b);
//        double penalty = grammarHeuristics(a, req.getLang());
//        double scoreRaw = Math.max(0, Math.min(1.0, base - penalty)) * 100.0;
//
//        // 👉 chọn 1 trong 2 dòng dưới:
//        double score = round(scoreRaw, 1);   // 1 chữ số thập phân
//        // double score = round(scoreRaw, 0); // số nguyên
//
//        String fb = feedback(a, b, req.getLang());
//        return ResponseEntity.ok(new GradeOpenResponse(score, fb));
//    }
//
//    private static double round(double value, int digits){
//        double f = Math.pow(10, digits);
//        return Math.round(value * f) / f;
//    }
//
//    // TODO: implement normalize/jaroWinkler/grammarHeuristics/feedback
//// đặt 1 biến dùng chung cho Jaro–Winkler
//private static final JaroWinklerSimilarity JWS = new JaroWinklerSimilarity();
//
//    private static String normalize(String s){
//        if (s == null) return "";
//        String t = s.toLowerCase(Locale.ROOT).trim();
//        // bỏ dấu tiếng Việt (NFD + bỏ combining marks)
//        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
//        // giữ chữ/số/khoảng trắng/dấu nháy; bỏ ký tự lạ
//        t = t.replaceAll("[^\\p{L}\\p{Nd}\\s']", " ");
//        // gộp khoảng trắng
//        t = t.replaceAll("\\s+", " ").trim();
//        return t;
//    }
//
//    private static double jaroWinkler(String a, String b){
//        Double s = JWS.apply(a, b);    // similarity 0..1
//        return s == null ? 0.0 : s;
//    }
//    private static boolean findWord(String text, String regex){
//        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(text);
//        return m.find();
//    }
//    private static double grammarHeuristics(String a, String lang){
//        double pen = 0.0;
//        if (lang != null && lang.toLowerCase(Locale.ROOT).startsWith("en")) {
//            // ✅ chủ ngữ + have/has (bắt đầu hoặc giữa câu đều bắt được)
//            if (findWord(a, "\\bi\\s+has\\b")) pen += 0.15;                 // I has  → sai
//            if (findWord(a, "\\b(you|we|they)\\s+has\\b")) pen += 0.12;     // you/we/they has → sai
//            if (findWord(a, "\\b(he|she|it)\\s+have\\b")) pen += 0.10;      // he/she/it have  → sai
//
//            // thiếu mạo từ trước 1 số danh từ phổ biến
//            if (findWord(a, "\\b(dog|cat|apple|university)\\b") &&
//                    !findWord(a, "\\b(a|an|the)\\s+(dog|cat|apple|university)\\b"))
//                pen += 0.05;
//
//            // nhiều khoảng trắng bất thường
//            if (a.contains("  ")) pen += 0.02;
//        }
//        return Math.min(pen, 0.30); // trần phạt 30%
//    }
//
//    private static String feedback(String a, String b, String lang){
//        if (lang != null && lang.toLowerCase(Locale.ROOT).startsWith("en")) {
//            if (findWord(a, "\\bi\\s+has\\b")) return "Dùng “I have …” thay vì “I has …”.";
//            if (findWord(a, "\\b(you|we|they)\\s+has\\b")) return "Sau you/we/they dùng “have”, không phải “has”.";
//            if (findWord(a, "\\b(he|she|it)\\s+have\\b")) return "Sau he/she/it dùng “has”, không phải “have”.";
//            if (findWord(a, "\\b(dog|cat|apple|university)\\b") &&
//                    !findWord(a, "\\b(a|an|the)\\s+(dog|cat|apple|university)\\b"))
//                return "Thiếu mạo từ: thêm “a/an/the” trước danh từ.";
//        }
//        if (jaroWinkler(a, b) > 0.95) return "Câu đúng. Rất tốt!";
//        return "Ổn rồi. Có thể diễn đạt tự nhiên hơn gần với đáp án mục tiêu.";
//    }
//    private static double tokenSimilarity(String a, String b){
//        String[] ta = a.split("\\s+");
//        String[] tb = b.split("\\s+");
//        if (ta.length==0 || tb.length==0) return 0;
//
//        // trung bình “mỗi từ bên A khớp tốt nhất với 1 từ bên B” (và ngược lại)
//        double sumA=0;
//        for (String wa: ta){
//            double best=0;
//            for (String wb: tb){
//                Double s = JWS.apply(wa, wb);
//                if (s!=null && s>best) best=s;
//            }
//            sumA += best;
//        }
//        double avgA = sumA/ta.length;
//
//        double sumB=0;
//        for (String wb: tb){
//            double best=0;
//            for (String wa: ta){
//                Double s = JWS.apply(wb, wa);
//                if (s!=null && s>best) best=s;
//            }
//            sumB += best;
//        }
//        double avgB = sumB/tb.length;
//        return (avgA + avgB)/2.0;   // 0..1
//    }
//}