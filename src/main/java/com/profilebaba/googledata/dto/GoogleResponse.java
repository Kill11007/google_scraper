package com.profilebaba.googledata.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.profilebaba.googledata.dto.deserializer.RecordDeserializer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = RecordDeserializer.class)
public class GoogleResponse {
  private List<Record> records;
}
