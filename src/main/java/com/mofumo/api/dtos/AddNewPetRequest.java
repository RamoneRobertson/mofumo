package com.mofumo.api.dtos;

import com.mofumo.api.enums.Species;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddNewPetRequest {
  private String name;
  private int ageYr;
  private BigDecimal weightKg;
  private Species species;
  private String breed;
  private String medicalConditions;
  private String specialInstruction;
  private String emergencyContactName;
  private String emergencyContactPhone;

}
