package com.example.infinityweb_be.util;

import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class YoutubeUrlConverter {

    private static final Pattern YOUTUBE_REGEX = Pattern.compile(
            "^.*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Chuyển đổi URL YouTube thông thường sang URL embed.
     * Nếu URL đã là embed hoặc không phải YouTube, trả về nguyên bản.
     *
     * @param originalUrl URL gốc từ người dùng nhập
     * @return URL embed hoặc URL gốc nếu không phải YouTube
     */
    public String convertToEmbedUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.isBlank()) {
            return originalUrl;
        }

        // Nếu URL đã là embed, không cần xử lý
        if (originalUrl.contains("youtube.com/embed/")) {
            return originalUrl;
        }

        // Trích xuất Video ID từ URL
        Matcher matcher = YOUTUBE_REGEX.matcher(originalUrl);
        if (matcher.find() && matcher.group(1).length() == 11) {
            String videoId = matcher.group(1);
            return "https://www.youtube.com/embed/" + videoId;
        }

        // Trả về URL gốc nếu không khớp với định dạng YouTube
        return originalUrl;
    }
}