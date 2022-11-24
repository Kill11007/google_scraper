package com.profilebaba.googledata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.service.impl.GoogleService;
import com.profilebaba.googledata.service.impl.GoogleService.GoogleVendor;
import com.profilebaba.googledata.service.impl.GoogleVendorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google/search")
@CrossOrigin("*")
public class GoogleSearchController {

  private final GoogleService googleService;
  private final GoogleVendorService googleVendorService;

  @GetMapping
  public ResponseEntity<List<GoogleVendor>> search(
      @RequestParam(value = "query", required = false) String query,
      @RequestParam(value = "category", required = false) String category,
      @RequestParam(value = "search_category", required = false) Integer searchCategory,
      @RequestParam(value = "location", required = false) String location,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "size", required = false, defaultValue = "50") Integer size) throws JsonProcessingException {
    List<GoogleVendor> googleBusinessInformation = googleService.getGoogleBusinessInformation(
          query, category, location, state, size, searchCategory);
    googleVendorService.saveVendorOnAllCategories(location, size);
    return ResponseEntity.ok(googleBusinessInformation);
  }
}
