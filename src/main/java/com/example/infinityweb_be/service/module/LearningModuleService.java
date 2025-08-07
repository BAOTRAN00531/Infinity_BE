package com.example.infinityweb_be.service.module;

import com.example.infinityweb_be.domain.course.Course;
import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleRequest;
import com.example.infinityweb_be.domain.dto.order.OrderStatus;
import com.example.infinityweb_be.domain.map.LearningModuleMapper;
import com.example.infinityweb_be.repository.CourseRepository;
import com.example.infinityweb_be.repository.LearningModuleRepository;
import com.example.infinityweb_be.repository.LessonRepository;
import com.example.infinityweb_be.repository.UserRepository;
import com.example.infinityweb_be.repository.order.OrderRepository;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.order.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final OrderRepository orderRepository;

    private final OrderService orderService; // 👈 thêm dòng này
    private final LearningModuleMapper moduleMapper; // 👈 thêm nếu dùng mapper


    @PersistenceContext
    private EntityManager entityManager;

    private void setSessionContext(int userId) {
        entityManager.createNativeQuery("EXEC sp_set_session_context 'user_id', :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    // === CREATE DTO ===
    @Transactional
    public LearningModuleDto createDto(LearningModuleRequest req, int adminId) {
        setSessionContext(adminId);

        // Map DTO → Entity
        LearningModule module = new LearningModule();
        module.setName(req.getName());
        module.setDescription(req.getDescription());
        module.setOrder(req.getOrder());
        module.setDuration(req.getDuration());
        module.setStatus(req.getStatus());

        // Gán Course nếu có
        if (req.getCourseId() != null) {
            Course course = courseRepository.findById(req.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + req.getCourseId()));
            module.setCourse(course);

            // 🔷 Đếm số module hiện có trong course để tính order
            Integer maxOrder = moduleRepository.findMaxOrderByCourseId(req.getCourseId());
            module.setOrder((maxOrder != null ? maxOrder : 0) + 1);
        } else {
            throw new RuntimeException("CourseId is required");
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
    public LearningModuleDto updateDto(Integer id, LearningModuleRequest req, int adminId) {
        setSessionContext(adminId);

        LearningModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found: " + id));

        // Kiểm tra trạng thái course cha
        Course course = module.getCourse();
        if (course != null && "inactive".equalsIgnoreCase(course.getStatus())) {
            // Nếu course cha đang inactive, không cho phép cập nhật trạng thái module
            if (req.getStatus() != null && !req.getStatus().equalsIgnoreCase(module.getStatus())) {
                throw new RuntimeException("Không thể thay đổi trạng thái module khi course cha đang inactive");
            }
        }

        // Cập nhật fields từ DTO
        module.setName(req.getName());
        module.setDescription(req.getDescription());
        module.setOrder(req.getOrder());
        module.setDuration(req.getDuration());
        module.setStatus(req.getStatus());

        // Cập nhật Course nếu client gửi courseId, hoặc clear nếu null
        if (req.getCourseId() != null) {
            Course newCourse = courseRepository.findById(req.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + req.getCourseId()));
            module.setCourse(newCourse);
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


//chjeck

    public List<LearningModuleDto> getByCourseIdDto(Integer courseId) {
        return moduleRepository.findByCourseId(courseId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

//====== STUDENT LESSON ======//
public List<LearningModuleDto> getModulesByCourseForStudent(Integer courseId, Integer userId) {
    boolean hasPurchased = orderRepository.existsByUserIdAndCourseIdAndStatus(userId, courseId, OrderStatus.PAID);
    if (!hasPurchased) {
        throw new AccessDeniedException("Bạn chưa mua khóa học này.");
    }

    return moduleRepository.findByCourseId(courseId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
}


}


