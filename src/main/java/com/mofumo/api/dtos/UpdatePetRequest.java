package com.mofumo.api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePetRequest {
  private String name;
  private int ageYr;
  private BigDecimal weightKg;
  private String medicalConditions;
  private String specialInstruction;
  private String emergencyContact;
  private String emergencyContactPhone;
}
