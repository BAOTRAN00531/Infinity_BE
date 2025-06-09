package com.example.infinityweb_be.service;

import com.example.infinityweb_be.domain.User;
import com.example.infinityweb_be.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserRepository userRepository;
    public UserDetailServiceImpl(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userService.handleGetAccountByEmail(usernameOrEmail);
        if (user == null) {
            user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }
        return new UserDetailCustom(user);
    }
}
