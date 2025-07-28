package com.moodtunes.apiserver.advice;

import com.moodtunes.apiserver.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(MethodArgumentNotValidException ex){

        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse badRequest = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequest);
    }
}
