package com.profilebaba.googledata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class Category {
  private Integer id;
  private String title;
  @JsonProperty("sub-catgeories")
  private List<Category> subCatgeories;
}
