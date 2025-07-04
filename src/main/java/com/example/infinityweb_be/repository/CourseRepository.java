package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByLanguage_Code(String code);


    // chạy count course trong page language
//    Long countByLanguageId(Integer languageId);
}
