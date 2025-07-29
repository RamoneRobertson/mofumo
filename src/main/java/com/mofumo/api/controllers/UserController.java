package com.mofumo.api.controllers;

import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
}
