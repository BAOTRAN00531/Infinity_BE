package com.example.infinityweb_be.util.error;

import com.example.infinityweb_be.domain.CustomExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomExceptionResponse handleGeneralException(Exception ex, HttpServletRequest request) {
        CustomExceptionResponse exceptionResponse = new CustomExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setPath(request.getRequestURI());
        return exceptionResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionResponse handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        CustomExceptionResponse exceptionResponse = new CustomExceptionResponse();
        BindingResult result = ex.getBindingResult();
        List<String> errors = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        exceptionResponse.setMessage(String.join(", ", errors));
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setPath(request.getRequestURI());
        return exceptionResponse;
    }
}