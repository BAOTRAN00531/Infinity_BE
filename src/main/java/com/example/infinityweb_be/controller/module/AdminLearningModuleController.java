package com.example.infinityweb_be.controller.module;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleRequest;
import com.example.infinityweb_be.repository.UserRepository;
//import com.example.infinityweb_be.service.;
import com.example.infinityweb_be.service.module.LearningModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class AdminLearningModuleController {


    private final AuthHelper authHelper;

    private final LearningModuleService moduleService;
    private final UserRepository userRepository;

    // Lấy tất cả modules hoặc theo courseId (đã trả về DTO)
    @GetMapping
    public List<LearningModuleDto> getModules(
            @RequestParam(required = false) Integer courseId,
            Principal principal) {
        if (courseId != null) {
            // If user is authenticated, include progress
            if (principal != null) {
                try {
                    Integer userId = getUserIdFromPrincipal(principal);
                    return moduleService.getByCourseIdDtoWithProgress(courseId, userId);
                } catch (Exception e) {
                    // If getting user ID fails, return without progress
                    return moduleService.getByCourseIdDto(courseId);
                }
            }
            return moduleService.getByCourseIdDto(courseId);
        }
        return moduleService.getAllDto();
    }

    private Integer getUserIdFromPrincipal(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }


    // Tạo mới module: nhận DTO, trả về DTO
    @PostMapping
    public LearningModuleDto create(@RequestBody LearningModuleRequest request,
                                    JwtAuthenticationToken token) {
        // Lấy adminId từ token
        String email = token.getName();
        int adminId = authHelper.getCurrentUserId(token);


        // moduleService.createDto sẽ map DTO → entity, lưu, rồi map entity → DTO
        return moduleService.createDto(request, adminId);
    }

    // Cập nhật module theo id
    @PutMapping("/{id}")
    public LearningModuleDto update(@PathVariable Integer id,
                                    @RequestBody LearningModuleRequest request,
                                    JwtAuthenticationToken token) {
        String email = token.getName();
        int adminId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        return moduleService.updateDto(id, request, adminId);
    }

    @GetMapping("/{id}")
    public LearningModuleDto getModuleById(@PathVariable Integer id) {
        return moduleService.getByIdDto(id);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        moduleService.delete(id);
    }
}
