package com.example.infinityweb_be.controller;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.dto.LearningModuleDto;
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
    private  LearningModuleDto learningModuleDto;
    private final LearningModuleService moduleService;
    private final UserRepository userRepository;

    // ✅ Lấy tất cả hoặc theo courseId nếu có
    @GetMapping
    public List<LearningModuleDto> getModules(@RequestParam(required = false) Integer courseId) {
        if (courseId != null) {
            return moduleService.getByCourseIdDto(courseId);
        }
        return moduleService.getAllDto();
    }

    @PostMapping
    public LearningModule create(@RequestBody LearningModule module, JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        return moduleService.create(module, adminId);
    }

    @PutMapping("/{id}")
    public LearningModule update(@PathVariable Integer id,
                                 @RequestBody LearningModule module,
                                 JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email).orElseThrow().getId();
        return moduleService.update(id, module, adminId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        moduleService.delete(id);
    }
}
