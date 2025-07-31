package com.mofumo.api.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentErrors(
          MethodArgumentNotValidException exception
  ){
    var errors = new HashMap<String, String>();
    exception.getBindingResult().getFieldErrors().forEach(
            error -> {
              errors.put(error.getField(), error.getDefaultMessage());
            }
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(
          HttpMessageNotReadableException exception
  ){
    var errors = new HashMap<String, String>();
    errors.put("Error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
          DataIntegrityViolationException exception
  ){
    // Check if the exception contains users_email_unique
    if(exception.getMostSpecificCause().getMessage().contains("users_email_unique")) {
      // Should return a status of 409 conflict
      return ResponseEntity.status(HttpStatus.CONFLICT).body(
              Map.of("Error", "Email address already in use.")
      );
    }
    // Else, it means some other field has invalid data
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
            Map.of("Error", "Data Integrity Violation " + exception.getMessage())
    );
  }

}
