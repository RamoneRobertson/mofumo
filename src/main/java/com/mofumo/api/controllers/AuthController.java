package com.mofumo.api.controllers;

import com.mofumo.api.dtos.LoginRequest;
import com.mofumo.api.exceptions.UserNotFoundException;
import com.mofumo.api.mappers.UserMapper;
import com.mofumo.api.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
        @Valid @RequestBody LoginRequest request
  ){
    authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getEmail(),
                  request.getPassword()
          )
    );
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
