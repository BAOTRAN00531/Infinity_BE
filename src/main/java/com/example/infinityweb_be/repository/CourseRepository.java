package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByLanguage_Code(String code);
}
