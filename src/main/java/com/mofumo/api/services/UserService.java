package com.mofumo.api.services;

import com.mofumo.api.exceptions.UserNotFoundException;
import com.mofumo.api.repositories.UserRepository;
import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UpdateUserRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user = userRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("User not found"));
    return new User(
            user.getEmail(),
            user.getPassword(),
            // authorities : things like permissions ect.. right now just return an empty list
            Collections.emptyList()
    );
  }

  public UserDto registerUser(RegisterUserRequest request){
    var user = userMapper.toEntity(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
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
