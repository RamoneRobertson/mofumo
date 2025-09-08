package com.mofumo.api.dtos;

import com.mofumo.api.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
  private Role role;
  private Boolean emailVerified;
  private Boolean active;
  private Instant createdAt;
  private Instant updatedAt;
}
