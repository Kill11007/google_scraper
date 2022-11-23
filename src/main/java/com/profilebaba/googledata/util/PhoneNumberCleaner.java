package com.profilebaba.googledata.util;

import java.util.List;

public class PhoneNumberCleaner {

  private static final List<String> INVALID_CHARS = List.of("0", "+91");

  public static String clean(String phoneNumber) {
    phoneNumber = phoneNumber.replace(" ", "");
    phoneNumber = phoneNumber.replace("-", "");
    if (phoneNumber.length() == 11 && phoneNumber.startsWith("0")) {
      phoneNumber = phoneNumber.substring(1);
    }
    if (phoneNumber.startsWith("+91")) {
      phoneNumber = phoneNumber.replace("+91", "");
    }
    return phoneNumber;
  }
}
