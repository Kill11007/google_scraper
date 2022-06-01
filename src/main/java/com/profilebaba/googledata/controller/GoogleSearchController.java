package com.profilebaba.googledata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.entity.Vendor;
import com.profilebaba.googledata.service.impl.GoogleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google/search")
public class GoogleSearchController {

  private final GoogleService googleService;

  @GetMapping("categories/{categoryId}/location/{locationId}")
  public ResponseEntity<List<Vendor>> search(@PathVariable Integer categoryId,
      @PathVariable Integer locationId) throws JsonProcessingException {
    List<Vendor> googleBusinessInformation = googleService.getGoogleBusinessInformation(categoryId,
        locationId);
    return ResponseEntity.ok(googleBusinessInformation);
  }
}
