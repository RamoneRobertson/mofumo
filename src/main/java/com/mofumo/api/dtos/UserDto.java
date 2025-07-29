package com.mofumo.api.dtos;

import com.mofumo.api.enums.UserType;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String phone;
  private String address;
  private String ward;
  private UserType userType;
  private Boolean emailVerified;
  private Boolean active;
  private Instant createdAt;
}
