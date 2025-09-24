package com.example.infinityweb_be;

import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InfinityWebBeApplication {
    private static final Logger log = LoggerFactory.getLogger(InfinityWebBeApplication.class);
    public static void main(String[] args) {

        SpringApplication.run(InfinityWebBeApplication.class, args);
    }


}
