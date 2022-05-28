package com.profilebaba.googledata.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GoogleBusinessTest {

  @Autowired
  private GoogleBusiness googleBusiness;

  @Test
  void test_search() throws JsonProcessingException {
    Stream.of(new Request(5, 0, "DJ in Najafgarh, Delhi, IN"),
        new Request(5, 0, "Plumber in Uttam Nagar, Delhi, IN"),
        new Request(5, 0, "Hardware Shop in Matiala, Delhi, IN"),
        new Request(5, 0, "Car Repair in Raja Garden, Delhi, IN"),
        new Request(5, 0, "ghodi wala in Shahdara, Delhi, IN"))
        .forEach(this::test_record);

  }

  void test_record(Request request) {
    Optional<GoogleResponse> search = Optional.empty();
    try {
      search = googleBusiness.search(request);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    assertNotNull(search);
    assertTrue(search.isPresent());
    assertNotNull(search.get().getRecords());
    assertEquals(5, search.get().getRecords().size());
    search.get().getRecords().forEach(
        record -> {
          assertNotNull(record);
          assertNotNull(record.getName());
          assertNotNull(record.getAddress());
//          log.info("Record: {}", record);
        }
    );
  }
}
