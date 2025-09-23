package com.example.infinityweb_be.storage;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
@Service
public class AudioStorageService {

    // Thư mục vật lý để lưu file (mặc định: ./uploads/audio)
    @Value("${app.audio.storage-dir:uploads/audio}")
    private String storageDir;

    // URL base để FE truy cập (mặc định: /uploads/audio)
    @Value("${app.audio.base-url:/uploads/audio}")
    private String baseUrl;

    public String saveBase64(String base64, String ext) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            Path dir = Paths.get(storageDir);
            Files.createDirectories(dir);
            String filename = UUID.randomUUID().toString().replace("-", "") + "." + (ext == null ? "mp3" : ext);
            Path file = dir.resolve(filename);
            Files.write(file, bytes);
            return baseUrl + "/" + filename; // ví dụ: /uploads/audio/xxxxx.mp3
        } catch (Exception e) {
            throw new RuntimeException("Save audio failed: " + e.getMessage(), e);
        }
    }
}