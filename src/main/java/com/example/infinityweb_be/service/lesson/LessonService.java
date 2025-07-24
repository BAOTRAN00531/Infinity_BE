// LessonService.java (using orderIndex)
package com.example.infinityweb_be.service.lesson;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.order.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LearningModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

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

    public LessonDto getLessonDto(int id, String username) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id " + id));

        Integer courseId = lesson.getModule().getCourse().getId();

        if (!isAdmin() && !orderRepository.hasValidOrder(username, courseId)) {
            throw new AccessDeniedException("Bạn chưa mua khoá học này hoặc đã hết hạn.");
        }

        return toDto(lesson);
    }


    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> getByModuleId(Integer moduleId) {
        return lessonRepository.findByModule_Id(moduleId);
    }

    public List<LessonDto> getAllDto(String username) {
        return getAllLessons().stream()
                .filter(lesson -> isAdmin() || hasAccess(username, lesson))
                .map(this::toDto)
                .toList();
    }

    public List<LessonDto> getByModuleIdDto(Integer moduleId, String username) {
        return getByModuleId(moduleId).stream()
                .filter(lesson -> isAdmin() || hasAccess(username, lesson))
                .map(this::toDto)
                .toList();
    }

    public Lesson createFromDto(LessonDto dto, int adminId) {
        setSessionContext(adminId);
        Lesson lesson = new Lesson();
        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setContent(dto.getContent());
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
        lessonRepository.deleteById(id);
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean hasAccess(String username, Lesson lesson) {
        Integer courseId = lesson.getModule().getCourse().getId();
        return orderRepository.hasValidOrder(username, courseId);
    }

    private LessonDto toDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setDescription(lesson.getDescription());
        dto.setContent(lesson.getContent());
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

}
