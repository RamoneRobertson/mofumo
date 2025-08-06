package com.mofumo.api.controllers;

import com.mofumo.api.dtos.JwtResponse;
import com.mofumo.api.dtos.LoginRequest;
import com.mofumo.api.exceptions.UserNotFoundException;
import com.mofumo.api.mappers.UserMapper;
import com.mofumo.api.repositories.UserRepository;
import com.mofumo.api.services.JwtService;
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
  private final JwtService jwtService;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(
        @Valid @RequestBody LoginRequest request
  ){
    // Authenticate the user email and password
    authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getEmail(),
                  request.getPassword()
          )
    );
     // After authenticating the user generate a new token
    var token = jwtService.generateToken(request.getEmail());
    return ResponseEntity.ok(new JwtResponse(token));
  }

  @PostMapping("/validate")
  // Pass the token using the Authorization header in the client request
  public Boolean validate(@RequestHeader("Authorization") String authHeader){
    var token = authHeader.replace("Bearer ", "");
    return jwtService.validateToken(token);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
