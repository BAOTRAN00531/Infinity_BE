package com.example.infinityweb_be.domain.dto;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class CustomExceptionResponse<T> {
    private HttpStatusCode statusCode;
    private String message;
    private T data;
    private String path;
    private LocalDateTime timestamp;
}
