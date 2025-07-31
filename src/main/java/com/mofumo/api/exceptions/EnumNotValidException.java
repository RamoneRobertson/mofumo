package com.mofumo.api.exceptions;

public class EnumNotValidException extends RuntimeException {
  public EnumNotValidException(String message) {
    super(message);
  }
}
