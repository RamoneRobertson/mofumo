package com.mofumo.api.controllers;

import com.mofumo.api.UserNotFoundException;
import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

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

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("User", "User not found")
    );
  }
}
