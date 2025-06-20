package com.example.infinityweb_be.config;
import com.example.infinityweb_be.service.UserDetailCustom;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
@RequiredArgsConstructor
public class SessionContextInterceptor implements HandlerInterceptor {

    private final DataSource dataSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetailCustom userDetail) {
            int userId = userDetail.getUser().getId();

            try (Connection conn = DataSourceUtils.getConnection(dataSource);
                 PreparedStatement ps = conn.prepareStatement("EXEC sp_set_session_context ?, ?")) {
                ps.setString(1, "user_id");
                ps.setInt(2, userId);
                ps.execute();
            }
        }
        return true;
    }
}