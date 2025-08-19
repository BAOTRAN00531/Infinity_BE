package com.example.infinityweb_be.controller.student.Progress;

import com.example.infinityweb_be.service.UserDetailCustom;
import com.example.infinityweb_be.service.UserService;
import com.example.infinityweb_be.service.progress.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/client/api/user/progress")
@RequiredArgsConstructor
public class UserProgressController {

    private final UserProgressService userProgressService;
    private final UserService userService; // ✅ Inject UserService

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Double> getCourseProgress(
            Principal principal, // ✅ Chỉ cần dùng Principal
            @PathVariable int courseId
    ) {
        // Kiểm tra principal có tồn tại (được xác thực)
        if (principal == null) {
            // Spring Security đã chặn request không có token, nhưng đây là một lớp phòng thủ
            return ResponseEntity.status(401).body(null);
        }

        // ✅ Dùng service để lấy userId
        int userId = userService.getUserIdFromPrincipal(principal);
        double progress = userProgressService.calculateCourseProgress(userId, courseId);

        return ResponseEntity.ok(progress);
    }
}