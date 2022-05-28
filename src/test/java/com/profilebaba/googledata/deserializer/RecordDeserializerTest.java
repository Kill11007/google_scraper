package com.profilebaba.googledata.deserializer;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Record;
import com.profilebaba.googledata.dto.deserializer.RecordDeserializer;
import java.io.File;
import java.net.URL;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecordDeserializerTest {

  private ObjectMapper mapper;
  private RecordDeserializer deserializer;

  @BeforeEach
  void setup() {
    mapper = new ObjectMapper();
    deserializer = new RecordDeserializer();
  }

  @Test
  @SneakyThrows()
  void test_deserialize() {
    String jsonFileName = "restaurants-json.json";
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(jsonFileName);
    JsonParser parser = mapper.getFactory().createParser(new File(resource.toURI()));
    DeserializationContext deserializationContext = mapper.getDeserializationContext();
    GoogleResponse googleResponse = deserializer.deserialize(parser, deserializationContext);
    List<Record> records = googleResponse.getRecords();
    assertNotNull(records);
    assertEquals(10, records.size());
    records.forEach(
        record -> {
          assertNotNull(record);
          assertNotNull(record.getName());
          assertNotNull(record.getAddress());
        }
    );

  }
}
