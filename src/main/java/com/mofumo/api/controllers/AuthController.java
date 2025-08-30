package com.mofumo.api.controllers;

import com.mofumo.api.config.JwtConfig;
import com.mofumo.api.dtos.JwtResponse;
import com.mofumo.api.dtos.LoginRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.mappers.UserMapper;
import com.mofumo.api.repositories.UserRepository;
import com.mofumo.api.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private final JwtConfig jwtConfig;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
  ){
    // Authenticate the user email and password
    authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getEmail(),
                  request.getPassword()
          )
    );

    var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
     // After authenticating the user generate a new token
    var accessToken = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    var cookie = new Cookie("refreshToken", refreshToken.toString());
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
    response.addCookie(cookie);

    return ResponseEntity.ok().body(new JwtResponse(accessToken.toString()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<JwtResponse> refreshToken(
          @CookieValue(value = "refreshToken") String refreshToken
  ) {
    var jwt = jwtService.parseToken(refreshToken);
    // First we need to validate the token
    if(jwt == null || jwt.isExpired()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // if the token is valid we need to extract the userId
    var userId = jwt.getUserId();

    // get the associated user from the repository
    var user = userRepository.findById(userId).orElseThrow();

    // generate an access token
    var accessToken = jwtService.generateAccessToken(user);

    // return an ok response
    return ResponseEntity.ok().body(new JwtResponse(accessToken.toString()));
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(){
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (Long) authentication.getPrincipal();
    var user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    var userDto = userMapper.toDto(user);
    return ResponseEntity.ok(userDto);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
