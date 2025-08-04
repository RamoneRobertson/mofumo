package com.mofumo.api.controllers;

import com.mofumo.api.exceptions.UserNotFoundException;
import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UpdateUserRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserDto> registerUser(
        @Valid @RequestBody RegisterUserRequest request,
        UriComponentsBuilder uriBuilder
  ){
    var userDto = userService.registerUser(request);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
    return ResponseEntity.created(uri).body(userDto);
  }

  @GetMapping("{userId}")
  public UserDto getUser(@PathVariable Long userId) {
    return userService.getUser(userId);
  }

  @PutMapping("/{userId}")
  public UserDto updateUser(
        @PathVariable Long userId,
        @Valid @RequestBody UpdateUserRequest request
  ){
    return userService.updateUser(userId, request);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("Error", "User not found.")
    );
  }
}
