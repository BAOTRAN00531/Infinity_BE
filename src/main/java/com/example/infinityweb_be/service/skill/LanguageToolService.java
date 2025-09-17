package com.example.infinityweb_be.service.skill;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.tuple.Pair;
import org.languagetool.rules.Category;
import org.languagetool.rules.CategoryId;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LanguageToolService {
    private final Map<String, JLanguageTool> cache = new ConcurrentHashMap<>();

    private JLanguageTool getTool(String lang){
        return cache.computeIfAbsent(
                (lang==null?"en":lang).toLowerCase(Locale.ROOT),
                k -> switch (k) {
                    case "en", "en-us", "en-gb" -> new JLanguageTool(new AmericanEnglish());
                    default -> new JLanguageTool(new AmericanEnglish()); // fallback
                }
        );
    }

    public List<RuleMatch> check(String text, String lang) throws IOException {
        return getTool(lang).check(text);
    }

    /** Tính penalty & tạo feedback ngắn từ các lỗi bắt được */
    public Pair<Integer, String> penaltyAndFeedback(String text, String lang) {
        try {
            List<RuleMatch> matches = check(text, lang);
            int pen = 0;
            String tip = null;

            for (RuleMatch m : matches) {
                // Lấy category -> id dưới dạng chuỗi an toàn cho mọi version
                String catStr = "";
                Category cat = m.getRule().getCategory();
                if (cat != null) {
                    CategoryId cid = cat.getId();           // object, không có getId()
                    if (cid != null) catStr = cid.toString();   // <-- dùng toString()
                    if (catStr.isEmpty() && cat.getName() != null) catStr = cat.getName();
                }
                // Fallback lần nữa bằng loại lỗi nếu cần
                if (catStr.isEmpty() && m.getRule().getLocQualityIssueType() != null) {
                    catStr = m.getRule().getLocQualityIssueType().toString();
                }

                String catU = catStr.toUpperCase(Locale.ROOT);
                if (catU.contains("GRAMMAR")) pen += 8;
                else if (catU.contains("TYPOS") || catU.contains("SPELL")) pen += 5;
                else if (catU.contains("PUNCT")) pen += 3;
                else pen += 2;

                if (tip == null) tip = m.getMessage();
            }

            pen = Math.min(pen, 30); // trần 30
            return Pair.of(pen, tip);

        } catch (Exception e) {
            return Pair.of(0, null);
        }
    }
}
