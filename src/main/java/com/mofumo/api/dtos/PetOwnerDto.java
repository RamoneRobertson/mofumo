package com.mofumo.api.dtos;

import lombok.Data;

@Data
public class PetOwnerDto {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String address;
}
