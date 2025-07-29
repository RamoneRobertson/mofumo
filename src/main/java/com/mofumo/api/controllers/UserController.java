package com.mofumo.api.controllers;

import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {
  public ResponseEntity<UserDto> registerUser(
        @RequestBody RegisterUserRequest request,
        UriComponentsBuilder uriBuilder
  ){
    return null;
  }
}
