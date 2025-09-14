package com.example.infinityweb_be.service.skill;

import com.example.infinityweb_be.domain.dto.SpeakingScoreDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeakingService {
    public SpeakingScoreDto score(MultipartFile audio, String text, String lang) {
// DEMO: điểm hợp lý để trình chiếu (chưa ASR)
        double completeness = Math.min(100, 60 + Math.min(40, text.length() * 0.5));
        double pron = 70; // sau 24/9 thay bằng MFCC+DTW so với TTS tham chiếu
        double fluency = 75; // có thể tính theo thời lượng & tỉ lệ im lặng


        double total = 0.5*pron + 0.3*completeness + 0.2*fluency;
        String[] tips = new String[]{"Giữ nhịp đều hơn ở giữa câu","Mở khẩu hình rõ ở âm dài"};
        return SpeakingScoreDto.builder()
                .scoreTotal(total).pronScore(pron).completeness(completeness).fluency(fluency).tips(tips).build();
    }
}
