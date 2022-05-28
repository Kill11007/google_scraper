package com.profilebaba.googledata.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoogleBusinessTest {

  @Autowired
  private GoogleBusiness googleBusiness;

  @Test
  void test_search() throws JsonProcessingException {
    Request request = new Request(5, 0, "ghodi wala in Shahdara, Delhi, IN");
    Optional<GoogleResponse> search = googleBusiness.search(request);
    assertNotNull(search);
    assertTrue(search.isPresent());
    assertNotNull(search.get().getRecords());
    assertEquals(5, search.get().getRecords().size());
    search.get().getRecords().forEach(
        record -> {
          assertNotNull(record);
          assertNotNull(record.getName());
          assertNotNull(record.getAddress());
        }
    );
  }
}
