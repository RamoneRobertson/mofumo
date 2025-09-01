package com.mofumo.api.dtos;

import com.mofumo.api.enums.Species;
import lombok.Data;

@Data
public class AddNewPetRequest {
  private UserDto owner;
  private String name;
  private Species species;
  private String emergencyContactName;
  private String emergencyContactPhone;
}
