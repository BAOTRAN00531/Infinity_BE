//package com.example.infinityweb_be.controller.client;
//
//
//import com.example.infinityweb_be.common.AuthHelper;
//import com.example.infinityweb_be.domain.dto.CourseDto;
//import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
//import com.example.infinityweb_be.service.course.CourseService;
//import com.example.infinityweb_be.service.module.LearningModuleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//public class ClientModuleController {
//
//    private final LearningModuleService moduleService;
//    private final AuthHelper authHelper;
//
//    // Lấy module theo courseId, tự động xác định quyền học viên
//    @GetMapping
//    public List<LearningModuleDto> getModulesByCourse(
//            @RequestParam("courseId") Integer courseId,
//            JwtAuthenticationToken token
//    ) {
//        String username = authHelper.getUsername(token); // hoặc token.getName()
//        return moduleService.getByCourseIdDto(courseId, username);
//    }
//}