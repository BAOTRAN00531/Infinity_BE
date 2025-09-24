// LessonService.java (using orderIndex)
package com.example.infinityweb_be.service.lesson;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.domain.dto.learn.LearnLessonDto;
import com.example.infinityweb_be.domain.dto.question.student.UserLessonQuestionProgressDto;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.enrollment.EnrollmentRepository;
import com.example.infinityweb_be.repository.order.OrderRepository;
import com.example.infinityweb_be.repository.progress.UserProgressRepository;
import com.example.infinityweb_be.repository.question.QuestionRepository;
import com.example.infinityweb_be.repository.question.UserQuestionProgressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LearningModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final OrderRepository orderRepository;
    private final EnrollmentRepository enrollmentRepository;
    //    private final YoutubeUrlConverter youtubeUrlConverter; // ✅ Thêm dòng này
    private final UserProgressRepository userProgressRepository; // ✅ THÊM repository này
    private final UserQuestionProgressRepository userQuestionProgressRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public Lesson findById(int id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id " + id));
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> getByModuleId(Integer moduleId) {
        return lessonRepository.findByModule_Id(moduleId);
    }

    public List<LessonDto> getAllDto() {
        return getAllLessons().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getByModuleIdDto(Integer moduleId) {
        return getByModuleId(moduleId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Lesson createFromDto(LessonDto dto, int adminId) {
        setSessionContext(adminId);
        Lesson lesson = new Lesson();
        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setContent(dto.getContent());

        // ✅ SỬA LẠI: Set videoUrl từ DTO vào Entity, và chuyển đổi sang embed URL nếu cần
//        String processedVideoUrl = youtubeUrlConverter.convertToEmbedUrl(dto.getVideoUrl());
        lesson.setVideoUrl(dto.getVideoUrl()); // Dùng URL đã được xử lý

        lesson.setType(dto.getType());
        lesson.setDuration(dto.getDuration());
        lesson.setStatus(dto.getStatus());

        int nextOrder = (dto.getOrderIndex() != null)
                ? dto.getOrderIndex()
                : lessonRepository.findMaxOrderByModule_Id(dto.getModuleId()) + 1;
        lesson.setOrderIndex(nextOrder);

        LearningModule moduleRef = moduleRepository.getReferenceById(dto.getModuleId());
        lesson.setModule(moduleRef);
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        lesson.setCreatedBy(admin);
        lesson.setCreatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    public Lesson updateFromDto(int id, LessonDto dto, int adminId) {
        setSessionContext(adminId);
        Lesson lesson = findById(id);
        if (dto.getName() != null) lesson.setName(dto.getName());
        if (dto.getDescription() != null) lesson.setDescription(dto.getDescription());
        if (dto.getContent() != null) lesson.setContent(dto.getContent());

        // ✅ SỬA LẠI: Cập nhật videoUrl từ DTO, và chuyển đổi sang embed URL nếu cần
        if (dto.getVideoUrl() != null) {
//            String processedVideoUrl = youtubeUrlConverter.convertToEmbedUrl(dto.getVideoUrl());
            lesson.setVideoUrl(dto.getVideoUrl()); // Dùng URL đã được xử lý
        }

        if (dto.getType() != null) lesson.setType(dto.getType());
        if (dto.getOrderIndex() != null) lesson.setOrderIndex(dto.getOrderIndex());
        if (dto.getDuration() != null) lesson.setDuration(dto.getDuration());
        if (dto.getStatus() != null) lesson.setStatus(dto.getStatus());
        if (dto.getModuleId() != null) {
            lesson.setModule(moduleRepository.getReferenceById(dto.getModuleId()));
        }
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        lesson.setUpdatedBy(admin);
        lesson.setUpdatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    public void delete(int id) {
        questionRepository.deleteByLesson_Id(id);

        lessonRepository.deleteById(id);
    }

    private LessonDto toDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setDescription(lesson.getDescription());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl()); // ✅ Set videoUrl vào DTO
        dto.setType(lesson.getType());
        dto.setOrderIndex(lesson.getOrderIndex());
        dto.setDuration(lesson.getDuration());
        dto.setStatus(lesson.getStatus());
        dto.setModuleId(lesson.getModule().getId());
        dto.setModuleName(lesson.getModule().getName());
        dto.setCreatedBy(lesson.getCreatedBy().getId());
        dto.setCreatedAt(lesson.getCreatedAt());
        dto.setUpdatedBy(lesson.getUpdatedBy() != null ? lesson.getUpdatedBy().getId() : null);
        dto.setUpdatedAt(lesson.getUpdatedAt());
        return dto;
    }

//====== LESSON STUDENT

//    public List<LessonDto> getLessonsByModuleForStudent(Integer moduleId, Integer userId) {
//        LearningModule module = moduleRepository.findById(moduleId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy module"));
//
//        Integer courseId = module.getCourse().getId();
//
//        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
//
//        if (!isEnrolled) {
//            throw new AccessDeniedException("Bạn chưa mua khóa học chứa module này.");
//        }
//
//        return lessonRepository.findByModule_Id(moduleId)
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }


    public List<LessonDto> getLessonsByModuleForStudent(Integer moduleId, Integer userId) {
        LearningModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy module"));

        Integer courseId = module.getCourse().getId();

        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);

        if (!isEnrolled) {
            throw new AccessDeniedException("Bạn chưa mua khóa học chứa module này.");
        }

        // Lấy tất cả lessons trong module
        List<Lesson> lessons = lessonRepository.findByModule_Id(moduleId);
        List<Integer> lessonIds = lessons.stream()
                .map(Lesson::getId)
                .collect(Collectors.toList());

        // ✅ Lấy danh sách completed lesson IDs chỉ với 1 query
        List<Integer> completedLessonIds = userProgressRepository.findCompletedLessonIds(userId, lessonIds);

        return lessons.stream()
                .map(lesson -> {
                    LessonDto dto = toDto(lesson);
                    // ✅ Kiểm tra nhanh trong memory
                    boolean isCompleted = completedLessonIds.contains(lesson.getId());
                    dto.setIsCompleted(isCompleted);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<LearnLessonDto> getLesson(Integer moduleId, Integer userId) {
        LearningModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy module"));

//        Integer courseId = module.getCourse().getId();
//
//        boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
//
//        if (!isEnrolled) {
//            throw new AccessDeniedException("Bạn chưa mua khóa học chứa module này.");
//        }

        // Lấy tất cả lessons trong module
        List<Lesson> lessons = lessonRepository.findByModule_Id(moduleId)
                .stream().sorted(Comparator.comparing(Lesson::getOrderIndex)).toList();
        List<Integer> lessonIds = lessons.stream().map(Lesson::getId).toList();
        Map<Integer, UserLessonQuestionProgressDto> map = userQuestionProgressRepository.getUserQuestionProgress(userId, lessonIds)
                .stream().collect(Collectors.toMap(UserLessonQuestionProgressDto::getLessonId, Function.identity()));

        return lessons.stream()
                .map(lesson -> {
                    Float progress = Optional.ofNullable(map.get(lesson.getId()))
                            .map(UserLessonQuestionProgressDto::getProgress)
                            .orElse(0f);
                    return new LearnLessonDto(
                            lesson.getId(),
                            lesson.getName(),
                            lesson.getDescription(),
                            lesson.getIcon(),
                            progress,
                            null
                    );
                })
                .collect(Collectors.toList());

    }


}