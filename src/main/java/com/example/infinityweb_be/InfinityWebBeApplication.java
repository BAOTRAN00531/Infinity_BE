package com.example.infinityweb_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InfinityWebBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfinityWebBeApplication.class, args);
    }
}
