package com.mofumo.api.dtos;

import com.mofumo.api.entities.User;
import com.mofumo.api.enums.Species;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PetDto {
  private Long id;
  private User owner;
  private String name;
  private Species species;
  private String breed;
  private Integer ageYr;
  private BigDecimal weightKg;
  private String medicalConditions;
  private String specialInstruction;
  private String emergencyContactName;
  private String emergencyContactPhone;

}
