package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.dto.LearningModuleDto;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.LearningModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class AdminLearningModuleController {

    private final LearningModuleService moduleService;
    private final UserRepository userRepository;

    // Lấy tất cả modules hoặc theo courseId (đã trả về DTO)
    @GetMapping
    public List<LearningModuleDto> getModules(@RequestParam(required = false) Integer courseId) {
        if (courseId != null) {
            return moduleService.getByCourseIdDto(courseId);
        }
        return moduleService.getAllDto();
    }

    // Tạo mới module: nhận DTO, trả về DTO
    @PostMapping
    public LearningModuleDto create(@RequestBody LearningModuleDto dto,
                                    JwtAuthenticationToken token) {
        // Lấy adminId từ token
        String email = token.getName();
        int adminId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        // moduleService.createDto sẽ map DTO → entity, lưu, rồi map entity → DTO
        return moduleService.createDto(dto, adminId);
    }

    // Cập nhật module theo id
    @PutMapping("/{id}")
    public LearningModuleDto update(@PathVariable Integer id,
                                    @RequestBody LearningModuleDto dto,
                                    JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        return moduleService.updateDto(id, dto, adminId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        moduleService.delete(id);
    }
}
