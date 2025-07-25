package com.example.infinityweb_be.controller.course;

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.dto.CourseDto;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class AdminCourseController {
    private final CourseService courseService;
    private final UserRepository userRepository;

    @GetMapping
    public List<Course> getAll() {
        return courseService.getAll();
    }

//    @GetMapping
//    public List<CourseDto> listCourses() {
//        return courseService.getAllCourses(); // Không cần login
//    }



    @PostMapping
    public Course create(@RequestBody Course course,
                         JwtAuthenticationToken auth) {
        String email = auth.getName(); // Lấy email từ JWT token
        User user = userRepository.findByEmail(email).orElseThrow();
        return courseService.create(course, user.getId());
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable int id,
                         @RequestBody Course course,
                         JwtAuthenticationToken auth) {
        String email = auth.getName(); // Lấy email từ JWT token
        User user = userRepository.findByEmail(email).orElseThrow();
        return courseService.update(id, course, user.getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        courseService.delete(id);
    }
}
