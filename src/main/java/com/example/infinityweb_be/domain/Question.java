// Question.java (Entity mapping for dbo.Questions - ĐÃ SỬA CHO ĐÚNG CSDL)
package com.example.infinityweb_be.domain;

import com.example.infinityweb_be.domain.course.Course;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Questions") // Tên bảng trong CSDL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    // SỬA LẠI: Ánh xạ đúng tên cột 'question_text'
    @Column(name = "question_text", nullable = false, length = 500)
    private String questionText; // Đổi tên trường từ 'content' -> 'questionText'

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_type_id", nullable = false)
    private QuestionType questionType;

    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    @Column(name = "audio_url", length = 255)
    private String audioUrl;

    @Column(name = "video_url", length = 255)
    private String videoUrl;

    // SỬA LẠI: Ánh xạ đúng tên cột 'difficulty'
    @Column(name = "difficulty", nullable = false, length = 10)
    private String difficulty; // Đổi tên trường từ 'level' -> 'difficulty'

    // SỬA LẠI: Ánh xạ đúng tên cột 'points'
    @Column(name = "points", nullable = false)
    private Integer points; // Đổi tên trường từ 'score' -> 'points'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionAnswer> answers;
}