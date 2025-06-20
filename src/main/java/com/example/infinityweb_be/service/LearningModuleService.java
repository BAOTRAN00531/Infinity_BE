package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.Course;
import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.LearningModuleDto;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.LearningModuleRepository;
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
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public LearningModule create(LearningModule module, int adminId) {
        setSessionContext(adminId);
        module.setCreatedAt(LocalDateTime.now());
        module.setCreatedBy(userRepository.findById(adminId).orElseThrow());
        return moduleRepository.save(module);
    }

    @Transactional
    public LearningModule update(Integer id, LearningModule newData, int adminId) {
        setSessionContext(adminId);
        LearningModule module = moduleRepository.findById(id).orElseThrow();
        module.setName(newData.getName());
        module.setDescription(newData.getDescription());
        module.setCourse(newData.getCourse());
        module.setOrder(newData.getOrder());
        module.setDuration(newData.getDuration());
        module.setStatus(newData.getStatus());
        module.setUpdatedBy(userRepository.findById(adminId).orElseThrow());
        module.setUpdatedAt(LocalDateTime.now());
        return moduleRepository.save(module);
    }

    public List<LearningModule> getByCourseId(Integer courseId) {
        return moduleRepository.findByCourseId(courseId);
    }

    public void delete(Integer id) {
        moduleRepository.deleteById(id);
    }

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
    public List<LearningModule> getAll() {
        return moduleRepository.findAll();
    }
    public LearningModuleDto toDto(LearningModule module) {
        return new LearningModuleDto(
                module.getId(),
                module.getName(),
                module.getDescription(),
                module.getCourse() != null ? module.getCourse().getId() : null,
                module.getCourse() != null ? module.getCourse().getName() : null,
                module.getOrder(),
                module.getDuration(),
                module.getStatus()
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
