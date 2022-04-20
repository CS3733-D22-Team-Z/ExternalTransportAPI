package edu.wpi.cs3733.D22.teamZ.api.exception;

import lombok.Getter;

public class ServiceException extends Exception {
  @Getter
  private final String message;

  public ServiceException(String message) {
    super(message);
    this.message = message;
  }
}
