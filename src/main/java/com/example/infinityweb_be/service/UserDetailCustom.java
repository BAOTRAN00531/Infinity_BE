package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailCustom implements UserDetails {

    private final User user;

    public UserDetailCustom(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Thay thế bằng vai trò thực tế từ User (nếu có)
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Đảm bảo User có getPassword()
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Hoặc user.getUsername() tùy theo cấu hình
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {          // Spring Security kiểm tra phương thức này
        return user.isActive();           // ⬅️  dùng isActive thay vì luôn true
    }

}