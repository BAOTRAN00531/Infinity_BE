package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Course;
import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.LearningModuleDto;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningModuleService {

    private final LearningModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    // === CREATE DTO ===
    @Transactional
    public LearningModuleDto createDto(LearningModuleDto dto, int adminId) {
        setSessionContext(adminId);

        // Map DTO → Entity
        LearningModule module = new LearningModule();
        module.setName(dto.getTitle());
        module.setDescription(dto.getDescription());
        module.setOrder(dto.getOrder());
        module.setDuration(dto.getDuration());
        module.setStatus(dto.getStatus());

        // Gán Course nếu có
        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + dto.getCourseId()));
            module.setCourse(course);
        }

        // Gán người tạo và thời gian
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found: " + adminId));
        module.setCreatedBy(admin);
        module.setCreatedAt(LocalDateTime.now());

        // Lưu và trả về DTO
        LearningModule saved = moduleRepository.save(module);
        return toDto(saved);
    }

    // === UPDATE DTO ===
    @Transactional
    public LearningModuleDto updateDto(Integer id, LearningModuleDto dto, int adminId) {
        setSessionContext(adminId);

        LearningModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found: " + id));

        // Cập nhật fields từ DTO
        module.setName(dto.getTitle());
        module.setDescription(dto.getDescription());
        module.setOrder(dto.getOrder());
        module.setDuration(dto.getDuration());
        module.setStatus(dto.getStatus());

        // Cập nhật Course nếu client gửi courseId, hoặc clear nếu null
        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + dto.getCourseId()));
            module.setCourse(course);
        } else {
            module.setCourse(null);
        }

        // Gán người sửa và thời gian
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found: " + adminId));
        module.setUpdatedBy(admin);
        module.setUpdatedAt(LocalDateTime.now());

        LearningModule updated = moduleRepository.save(module);
        return toDto(updated);
    }

    // === GET & DELETE còn dùng entity nếu cần ===
    public List<LearningModule> getAll() {
        return moduleRepository.findAll();
    }

    public List<LearningModule> getByCourseId(Integer courseId) {
        return moduleRepository.findByCourseId(courseId);
    }

    @Transactional
    public void delete(Integer moduleId) {
        // 1. Xoá hết lessons liên quan
        lessonRepository.deleteByModule_Id(moduleId);
        // 2. Xoá module
        moduleRepository.deleteById(moduleId);
    }

    // === CHUYỂN ENTITY → DTO ===
    public LearningModuleDto toDto(LearningModule module) {
        long count = lessonRepository.countByModule_Id(module.getId());
        return new LearningModuleDto(
                module.getId(),
                module.getName(),                // sẽ là title trong DTO
                module.getDescription(),
                module.getCourse() != null ? module.getCourse().getId() : null,
                module.getCourse() != null ? module.getCourse().getName() : null,
                module.getOrder(),
                module.getDuration(),
                module.getStatus(),
                count
        );
    }

    public List<LearningModuleDto> getAllDto() {
        return moduleRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<LearningModuleDto> getByCourseIdDto(Integer courseId) {
        return moduleRepository.findByCourseId(courseId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
