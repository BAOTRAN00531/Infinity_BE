package com.example.infinityweb_be.service.progress;

import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.progress.UserProgress;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.progress.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProgressService {
    private final LessonRepository lessonRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;

    public double calculateCourseProgress(int userId, int courseId) {
        long totalLessons = lessonRepository.countByCourseId(courseId);
        if (totalLessons == 0) {
            return 0.0;
        }

        List<Integer> lessonIds = lessonRepository.findLessonIdsByCourseId(courseId);
        if (lessonIds == null || lessonIds.isEmpty()) {
            return 0.0;
        }

        long completedLessons = userProgressRepository.countCompletedLessons(userId, lessonIds);
        return ((double) completedLessons / totalLessons) * 100;
    }

//    public void markLessonAsCompleted(Integer userId, Integer lessonId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Lesson lesson = lessonRepository.findById(lessonId)
//                .orElseThrow(() -> new RuntimeException("Lesson not found"));
//
//        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndEntityIdAndEntityType(userId, lessonId, "lesson");
//
//        if (existingProgress.isPresent()) {
//            UserProgress progress = existingProgress.get();
//            progress.setProgressPercentage(BigDecimal.valueOf(100.00));
//            progress.setLastUpdated(LocalDateTime.now());
//            userProgressRepository.save(progress);
//        } else {
//            UserProgress newProgress = new UserProgress();
//            newProgress.setUser(user);
//            newProgress.setEntityType("lesson");
//            newProgress.setEntityId(lessonId);
//            newProgress.setProgressPercentage(BigDecimal.valueOf(100.00));
//            newProgress.setLastUpdated(LocalDateTime.now());
//            userProgressRepository.save(newProgress);
//        }
//    }

    public void markLessonAsCompleted(Integer userId, Integer lessonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndEntityIdAndEntityType(userId, lessonId, "lesson");

        if (existingProgress.isPresent()) {
            UserProgress progress = existingProgress.get();
            progress.setProgressPercentage(BigDecimal.valueOf(100.00));
            progress.setLastUpdated(LocalDateTime.now());
            userProgressRepository.save(progress);
        } else {
            UserProgress newProgress = new UserProgress();
            newProgress.setUser(user);
            newProgress.setEntityType("lesson");
            newProgress.setEntityId(lessonId);
            newProgress.setProgressPercentage(BigDecimal.valueOf(100.00));
            newProgress.setLastUpdated(LocalDateTime.now());
            userProgressRepository.save(newProgress);
        }

        // ✅ Cập nhật trạng thái trên lesson entity (optional)
        lesson.setIsCompleted(true);
        lessonRepository.save(lesson);
    }

}