package com.profilebaba.googledata.service.impl;

import static java.text.MessageFormat.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.client.GoogleBusinessClient;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import com.profilebaba.googledata.mapper.GoogleResponseMapper;
import com.profilebaba.googledata.util.ApplicationUtility;
import com.profilebaba.googledata.util.QuerySeparator;
import com.profilebaba.googledata.util.QuerySeparator.SearchQueryPhrase;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GoogleService {

  private final GoogleBusinessClient googleBusiness;
  private final GoogleResponseMapper responseMapper;
  private final ProfileBabaAsyncVendorService profileBabaAsyncVendorService;
  private final QuerySeparator querySeparator;

  public GoogleVendor getGoogleBusinessInformation(String query, String location)
      throws JsonProcessingException {
    Optional<GoogleResponse> search = search(query, location);
    log.info("search: {}", search);
    return search
        .map(
            googleResponse ->
                responseMapper.responseToGoogleVendor(googleResponse.getRecords().get(0)))
        .orElseGet(() -> GoogleVendor.builder().build());
  }

  public List<GoogleVendor> getGoogleBusinessInformation(String query)
      throws JsonProcessingException {
    GoogleVendor search = getGoogleBusinessInformationNearBy(query);
    log.info("search: {}", search);
    return searchMetroStations("metro stations near " + search.address, search.address, 5)
        .map(responseMapper::responseToGoogleVendor)
        .orElseGet(() -> List.of(GoogleVendor.builder().build()))
        .stream()
        .filter(googleVendor -> googleVendor.category.equalsIgnoreCase("Subway station"))
        .toList();
  }

  public GoogleVendor getGoogleBusinessInformationNearBy(String query)
      throws JsonProcessingException {
    Optional<GoogleResponse> search = search(query);
    log.info("search: {}", search);
    GoogleVendor vendor =
        search
            .map(
                googleResponse ->
                    responseMapper.responseToGoogleVendor(googleResponse.getRecords().get(0)))
            .orElseGet(() -> GoogleVendor.builder().build());
    return vendor;
  }

  public List<GoogleVendor> getGoogleBusinessInformation(
      String query,
      String category,
      String location,
      String state,
      Integer size,
      Integer searchCategory)
      throws JsonProcessingException {
    location = ApplicationUtility.fixLocation(location);
    Optional<GoogleResponse> search;
    if (query == null) {
      search = search(category, location, size);
    } else {
      SearchQueryPhrase phrase = querySeparator.getPhrase(query, location);
      search = search(phrase.getQuery(), phrase.getLocation(), size);
    }
    if (search.isPresent()) {
      List<GoogleVendor> googleVendors = responseMapper.responseToGoogleVendor(search.get());
      // async below
      profileBabaAsyncVendorService.saveVendors(googleVendors, searchCategory, location);
      return googleVendors;
    }
    return List.of();
  }

  private Optional<GoogleResponse> search(String category, String location, Integer size)
      throws JsonProcessingException {
    final String QUERY = "{0} near {1}";
    final int MAX_CALL = 3;
    int count = 0;
    Optional<GoogleResponse> search;
    do {
      Request request = new Request(size, 0, format(QUERY, category, location));
      search = googleBusiness.search(request).map(this::filterGoogleSearchResponse);
      if (!location.contains(",")) break;
      location = location.substring(location.indexOf(", "));
      count++;
    } while (!isGoogleResponsePresent(search) && count < MAX_CALL);
    return search;
  }

  private Optional<GoogleResponse> searchMetroStations(
      String category, String location, Integer size) throws JsonProcessingException {
    final String QUERY = "{0} near {1}";
    Optional<GoogleResponse> search;
    Request request = new Request(size, 0, format(QUERY, category, location));
    search = googleBusiness.search(request);
    return search;
  }

  private Optional<GoogleResponse> search(String query, String location)
      throws JsonProcessingException {
    final String QUERY = "{0} near {1}";
    Request request = new Request(1, 0, format(QUERY, query, location));
    return googleBusiness.search(request);
  }

  private Optional<GoogleResponse> search(String query) throws JsonProcessingException {
    final String QUERY = "{0}";
    Request request = new Request(1, 0, format(QUERY, query));
    return googleBusiness.search(request);
  }

  private GoogleResponse filterGoogleSearchResponse(GoogleResponse googleResponse) {
    return new GoogleResponse(
        googleResponse.getRecords().stream()
            .filter(record -> Objects.nonNull(record.getPhone()))
            .toList());
  }

  // this method also has side effects
  private Boolean isGoogleResponsePresent(Optional<GoogleResponse> googleResponse) {
    return googleResponse.filter(response -> !response.getRecords().isEmpty()).isPresent();
  }

  @Data
  @Builder
  public static class GoogleVendor {
    private String name;
    private String phone;
    private String category;
    private String address;
    private String email;
    private String latitude;
    private String longitude;
  }
}
