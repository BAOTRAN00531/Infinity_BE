package com.example.infinityweb_be.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class AudioFileService {

    private static final String AUDIO_UPLOAD_DIR = "src/main/resources/static/uploads/audio/";
    private static final String AUDIO_URL_PREFIX = "/uploads/audio/";

    public String saveAudioFromBase64(String audioBase64, String text, String languageCode) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            createDirectoryIfNotExists();
            
            // Tạo tên file unique
            String fileName = generateFileName(text, languageCode);
            String filePath = AUDIO_UPLOAD_DIR + fileName;
            
            // Decode base64 và lưu file
            byte[] audioData = Base64.getDecoder().decode(audioBase64);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(audioData);
            }
            
            // Trả về URL để truy cập file
            return AUDIO_URL_PREFIX + fileName;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to save audio file: " + e.getMessage(), e);
        }
    }

    public String saveAudioFile(MultipartFile file, String text, String languageCode) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            createDirectoryIfNotExists();
            
            // Tạo tên file unique
            String fileName = generateFileName(text, languageCode);
            String filePath = AUDIO_UPLOAD_DIR + fileName;
            
            // Lưu file
            file.transferTo(new File(filePath));
            
            // Trả về URL để truy cập file
            return AUDIO_URL_PREFIX + fileName;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to save audio file: " + e.getMessage(), e);
        }
    }

    public void deleteAudioFile(String audioUrl) {
        if (audioUrl != null && audioUrl.startsWith(AUDIO_URL_PREFIX)) {
            String fileName = audioUrl.substring(AUDIO_URL_PREFIX.length());
            String filePath = AUDIO_UPLOAD_DIR + fileName;
            
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                // Log error nhưng không throw exception
                System.err.println("Failed to delete audio file: " + e.getMessage());
            }
        }
    }

    private void createDirectoryIfNotExists() throws IOException {
        Path directory = Paths.get(AUDIO_UPLOAD_DIR);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    private String generateFileName(String text, String languageCode) {
        // Tạo tên file an toàn từ text
        String safeText = StringUtils.cleanPath(text)
                .replaceAll("[^a-zA-Z0-9\\s-]", "")
                .replaceAll("\\s+", "_")
                .toLowerCase();
        
        // Giới hạn độ dài tên file
        if (safeText.length() > 50) {
            safeText = safeText.substring(0, 50);
        }
        
        // Thêm timestamp và UUID để đảm bảo unique
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return String.format("%s_%s_%s_%s.mp3", languageCode, safeText, timestamp, uuid);
    }

    public boolean isAudioFileExists(String audioUrl) {
        if (audioUrl == null || !audioUrl.startsWith(AUDIO_URL_PREFIX)) {
            return false;
        }
        
        String fileName = audioUrl.substring(AUDIO_URL_PREFIX.length());
        String filePath = AUDIO_UPLOAD_DIR + fileName;
        return Files.exists(Paths.get(filePath));
    }
} 