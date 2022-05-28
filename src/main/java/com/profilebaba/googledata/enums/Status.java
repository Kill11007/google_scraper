package com.profilebaba.googledata.enums;

import java.util.Optional;
import lombok.Getter;

public enum Status {
  IN_PROGRESS("I"),
  COMPLETED("C"),
  FAILED("F"),
  NOT_STARTED("N");

  @Getter
  private String status;

  Status(String status) {
    this.status = status;
  }

  public static Status of(String response) {
    String oResponse = Optional.ofNullable(response).orElse("");
    switch (oResponse) {
      case "I":
        return IN_PROGRESS;
      case "C":
        return COMPLETED;
      case "F":
        return FAILED;
      default:
        return NOT_STARTED;
    }
  }
}
