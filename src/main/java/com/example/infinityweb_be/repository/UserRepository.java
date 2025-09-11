package com.example.infinityweb_be.repository;

import com.example.infinityweb_be.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);


    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username); // nếu login bằng username thì cần thêm cái này

}
