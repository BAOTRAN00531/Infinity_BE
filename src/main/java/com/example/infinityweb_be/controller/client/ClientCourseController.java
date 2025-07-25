package com.example.infinityweb_be.controller.client;

import com.example.infinityweb_be.common.AuthHelper;
import com.example.infinityweb_be.domain.dto.CourseDto;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import com.example.infinityweb_be.service.course.CourseService;
import com.example.infinityweb_be.service.module.LearningModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/client/api/course")
@RequiredArgsConstructor
public class ClientCourseController {

    private final CourseService courseService;

    // Lấy tất cả khoá học cho học viên (dạng xem thử)
    @GetMapping
    public List<CourseDto> getCoursesForClient() {
        return courseService.getAllCourses();
    }

    // (Tuỳ chọn) Lấy chi tiết 1 khóa học
    @GetMapping("/{id}")
    public CourseDto getCourseDetail(@PathVariable int id) {
        return courseService.getDtoById(id);
    }
}

