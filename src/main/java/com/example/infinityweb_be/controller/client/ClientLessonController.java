//package com.example.infinityweb_be.controller.client;
//
//
//import com.example.infinityweb_be.common.AuthHelper;
//import com.example.infinityweb_be.domain.dto.CourseDto;
//import com.example.infinityweb_be.domain.dto.LessonDto;
//import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
//import com.example.infinityweb_be.service.course.CourseService;
//import com.example.infinityweb_be.service.lesson.LessonService;
//import com.example.infinityweb_be.service.module.LearningModuleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/api/client/lessons")
//@RequiredArgsConstructor
//public class ClientLessonController {
//
//    private final LessonService lessonService;
//    private final AuthHelper authHelper;
//
//    // Trả về danh sách bài học trong 1 module
//    @GetMapping
//    public List<LessonDto> getLessonsByModule(
//            @RequestParam("moduleId") Integer moduleId,
//            JwtAuthenticationToken token
//    ) {
//        String username = authHelper.getUsername(token);
//        return lessonService.getLessonsByModuleIdForUser(moduleId, username);
//    }
//}
