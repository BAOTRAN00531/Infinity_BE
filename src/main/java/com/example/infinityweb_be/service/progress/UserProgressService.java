package com.example.infinityweb_be.service.progress;

import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.progress.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProgressService {
    private final LessonRepository lessonRepository;
    private final UserProgressRepository userProgressRepository;

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
}