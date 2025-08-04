package com.mofumo.api.controllers;

import com.mofumo.api.dtos.LoginRequest;
import com.mofumo.api.exceptions.UserNotFoundException;
import com.mofumo.api.mappers.UserMapper;
import com.mofumo.api.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
        @Valid @RequestBody LoginRequest request
  ){
    var user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("Error", "User not found.")
    );
  }
}
