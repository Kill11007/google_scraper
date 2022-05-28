package com.profilebaba.googledata.deserializer;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profilebaba.googledata.dto.GoogleResponse;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GoogleResponseDeserializer {

  private ObjectMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new ObjectMapper();
  }

  @Test
  @SneakyThrows
  void test_googleResponse() {
    String jsonFileName = "restaurants-json.json";
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(jsonFileName);
    Path path = Paths.get(resource.toURI());
    String json = Files.readString(path, StandardCharsets.UTF_8);
    GoogleResponse googleResponse = mapper.readValue(json, GoogleResponse.class);
    assertNotNull(googleResponse);
    assertNotNull(googleResponse.getRecords());
    assertEquals(10, googleResponse.getRecords().size());

  }

}
