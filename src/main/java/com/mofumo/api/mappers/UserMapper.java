package com.mofumo.api.mappers;

import com.mofumo.api.dtos.RegisterUserRequest;
import com.mofumo.api.dtos.UserDto;
import com.mofumo.api.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toEntity(RegisterUserRequest request);
  UserDto toDto(User user);
}
