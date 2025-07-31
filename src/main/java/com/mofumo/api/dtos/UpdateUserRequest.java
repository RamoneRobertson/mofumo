package com.mofumo.api.dtos;

import com.mofumo.api.enums.Ward;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
  @Email(message = "Email must use a valid format.")
  @NotBlank(message = "Email is required.")
  private String email;

  @NotBlank(message = "First name is required.")
  @Pattern(
          regexp = "^[A-Za-z][A-Za-z'\\- ]{0,48}[A-Za-z]$",
          message = "First name must be 2–50 letters and may include hyphens, spaces, or apostrophes."
  )
  private String firstName;

  @NotBlank(message = "Last name is required.")
  @Pattern(
          regexp = "^[A-Za-z][A-Za-z'\\- ]{0,48}[A-Za-z]$",
          message = "Last name must be 2–50 letters and may include hyphens, spaces, or apostrophes."
  )
  private String lastName;

  @NotBlank(message = "Phone number is required.")
  @Pattern(
          regexp = "^\\d{2,4}-\\d{2,4}-\\d{3,4}$",
          message = "Phone number must be in the format 090-1234-5678"
  )
  private String phone;

  @NotBlank(message = "Address is required.")
  @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters.")
  @Pattern(
          regexp = "^[\\p{L}0-9\\-\\.,\\s]+$",
          message = "Address contains invalid characters."
  )
  private String address;

  @Size(max = 2, message = "Preferred language can only have a maximum of 2 characters. (e.g. jp,en)")
  private String preferredLang;

  private Ward ward;
  private String lineId;
}
