package com.profilebaba.googledata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
  private String name;
  private String googleCategory;
  private String phone;
  private String address;
  private String latitude;
  private String longitude;
  private String jsonResponse;
}
