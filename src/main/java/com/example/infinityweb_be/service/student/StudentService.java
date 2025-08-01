package com.example.infinityweb_be.service.student;

import com.example.infinityweb_be.domain.dto.student.StudentCourseProgressDto;
import com.example.infinityweb_be.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final CourseRepository courseRepository;

    public List<StudentCourseProgressDto> getDashboardCourses(Integer userId) {
        return courseRepository.findStudentDashboardCourses(userId);
    }
}
