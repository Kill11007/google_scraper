package com.profilebaba.googledata.config;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class QueryConfiguration {
  @Value("${query.prepositions}")
  List<String> prepositions;
  List<String> punctuationsAndDash = List.of("-",",");
}
