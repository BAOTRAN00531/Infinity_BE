//package com.example.infinityweb_be.domain.enrollment;
//
//import com.example.infinityweb_be.domain.User;
//import com.example.infinityweb_be.domain.course.Course;
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "Enrollment")
//@Data
//public class Enrollment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "enrolled_at")
//    private LocalDateTime enrolledAt;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "course_id", insertable = false, updatable = false)
//    private Course course;
//
//    @Column(name = "user_id")
//    private Integer userId;
//
//    @Column(name = "course_id")
//    private Integer courseId;
//}
