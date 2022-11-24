package com.profilebaba.googledata.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ApplicationUtility {
  public static String fixLocation(String location) {
    String[] parts = location.split(", ");
    Set<String> locationPartSet = new LinkedHashSet<>();
    Arrays.stream(parts).map(String::trim).forEach(locationPartSet::add);
    return String.join(", ", locationPartSet);
  }
}
