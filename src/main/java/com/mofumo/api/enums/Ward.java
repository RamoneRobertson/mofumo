package com.mofumo.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mofumo.api.exceptions.EnumNotValidException;

public enum Ward {
  ADACHI, ARAKAWA, BUNKYO, CHIYODA, CHUO, EDOGAWA,
  ITABASHI, KATSUSHIKA, KITA, KOTO, MEGURO, MINATO,
  NAKANO, NERIMA, OTA, SETAGAYA, SHIBUYA, SHINAGAWA,
  SHINJUKU, SUGINAMI, SUMIDA, TAITO, TOSHIMA;

  @JsonCreator
  public static Ward from(String value){
    // Loop through each ward
    for(Ward ward : Ward.values()){
      // check to see if the value matches, ignore case
      if(ward.toString().equalsIgnoreCase(value)){
        // If there is a match return the ward
        return ward;
      }
    }
    // If the value doesn't match any ward throw an exception
    throw new EnumNotValidException("Invalid ward: " + value);
  }
}
