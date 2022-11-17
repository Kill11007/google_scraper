package com.profilebaba.googledata.util;

import java.util.List;
import java.util.Optional;

public class PhoneNumberCleaner {

  private static final List<String> INVALID_CHARS = List.of("0", "+91");

  public static String clean(String phoneNumber) {
    Optional<String> invalidChar =
        INVALID_CHARS.stream().filter(phoneNumber::startsWith).findFirst();
    return invalidChar.map(s -> phoneNumber.substring(s.length() + 1)).orElse(phoneNumber);
  }
}
