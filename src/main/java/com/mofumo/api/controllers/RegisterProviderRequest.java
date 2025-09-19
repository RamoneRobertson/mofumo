package com.mofumo.api.controllers;

import com.mofumo.api.entities.User;
import lombok.Data;
import java.util.Map;

@Data
public class RegisterProviderRequest {
  private Long userId;
  private String businessName;
  private String address;
  private String description;
  private Map<String, String> languagesSpoken;
  private Map<String, String> serviceTypes;
  private Map<String, String> serviceAreas;
  private int basePrice;
}
