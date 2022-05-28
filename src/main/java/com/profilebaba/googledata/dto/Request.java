package com.profilebaba.googledata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
  private Integer totalRecords;
  private Integer offset;
  private String query;
}
