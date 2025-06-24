package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.Lesson;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.LessonDto;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LearningModuleRepository moduleRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

//    @Transactional
//    public Lesson create(Lesson lesson, int adminId) {
//        setSessionContext(adminId);
//        lesson.setCreatedAt(LocalDateTime.now());
//        lesson.setCreatedBy(userRepository.findById(adminId).orElseThrow());
//        return lessonRepository.save(lesson);
//    }
//
//    @Transactional
//    public Lesson update(Integer id, Lesson newData, int adminId) {
//        setSessionContext(adminId);
//        Lesson lesson = lessonRepository.findById(id).orElseThrow();
//        lesson.setName(newData.getName());
//        lesson.setDescription(newData.getDescription());
//        lesson.setModule(newData.getModule());
//        lesson.setUpdatedBy(userRepository.findById(adminId).orElseThrow());
//        lesson.setUpdatedAt(LocalDateTime.now());
//        return lessonRepository.save(lesson);
//    }

    public List<Lesson> getByModuleId(Integer moduleId) {
        return lessonRepository.findByModuleId(moduleId);
    }

    public void delete(Integer id) {
        lessonRepository.deleteById(id);
    }

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }



    // Mapping trong Service
    public LessonDto toDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setName(lesson.getName());
        dto.setDescription(lesson.getDescription());
        dto.setModuleId(lesson.getModule().getId());
        dto.setModuleName(lesson.getModule().getName());
        return dto;
    }

    public List<LessonDto> getByModuleIdDto(Integer moduleId) {
        return getByModuleId(moduleId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public Lesson findById(int id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id " + id));
    }

    @Transactional
    public Lesson createFromDto(LessonDto dto, int adminId) {
        // Thiết lập admin_id vào session context cho trigger
        setSessionContext(adminId);

        Lesson l = new Lesson();
        l.setName(dto.getName());
        l.setDescription(dto.getDescription());
        l.setContent(dto.getContent());

        // Map các trường mới
        l.setType(dto.getType());
        l.setOrder(dto.getOrder() != null ? dto.getOrder() : 1);
        l.setDuration(dto.getDuration());
        l.setStatus(dto.getStatus());

        // Quan hệ và audit
        l.setModule(moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found")));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        l.setCreatedBy(admin);
        l.setCreatedAt(LocalDateTime.now());
        // updatedBy/updatedAt giữ null cho create

        return lessonRepository.save(l);
    }

    @Transactional
    public Lesson updateFromDto(int id, LessonDto dto, int adminId) {
        // Thiết lập admin_id vào session context cho trigger
        setSessionContext(adminId);

        Lesson l = findById(id);
        l.setName(dto.getName());
        l.setDescription(dto.getDescription());
        l.setContent(dto.getContent());

        // Cập nhật các trường mới nếu có
        if (dto.getType() != null)     l.setType(dto.getType());
        if (dto.getOrder() != null)    l.setOrder(dto.getOrder());
        if (dto.getDuration() != null) l.setDuration(dto.getDuration());
        if (dto.getStatus() != null)   l.setStatus(dto.getStatus());

        // Quan hệ và audit
        l.setModule(moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found")));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        l.setUpdatedBy(admin);
        l.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(l);
    }


}
