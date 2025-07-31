package com.mofumo.api.services;

import com.mofumo.api.exceptions.UserNotFoundException;
import com.mofumo.api.repositories.UserRepository;
import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UpdateUserRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserMapper userMapper;
  private final UserRepository userRepository;

  public UserDto registerUser(RegisterUserRequest request){
    var user = userMapper.toEntity(request);
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  public UserDto getUser(Long userId){
    var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    return userMapper.toDto(user);
  }

  public UserDto updateUser(Long userId, UpdateUserRequest request){
    var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    userMapper.update(request, user);
    userRepository.save(user);
    return userMapper.toDto(user);
  }
}
