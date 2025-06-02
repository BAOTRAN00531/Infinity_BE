package com.example.infinityweb_be.util.error;

import com.example.assigment_java6.domain.CustomExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;/**/
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
    public CustomExceptionResponse handleGeneralException(Exception ex, HttpServletRequest request,MethodArgumentNotValidException methodArgumentNotValidException) {
        CustomExceptionResponse exceptionResponse = new CustomExceptionResponse();
        exceptionResponse.setMessage(methodArgumentNotValidException.getBody().getDetail());
        exceptionResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setPath(request.getRequestURI());
        return exceptionResponse;
    }
}

