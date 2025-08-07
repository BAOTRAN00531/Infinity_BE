package com.example.infinityweb_be.controller.UploadImage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    @Value("${upload.path:uploads}") // thư mục mặc định là /uploads
    private String uploadPath;

    @PostMapping
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, filename);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn tương đối để FE dùng
        return Map.of("url", "/uploads/" + filename);
    }
}
