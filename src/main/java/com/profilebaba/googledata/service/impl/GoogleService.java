package com.profilebaba.googledata.service.impl;

import static java.text.MessageFormat.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.client.GoogleBusinessClient;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Record;
import com.profilebaba.googledata.dto.Request;
import com.profilebaba.googledata.mapper.GoogleResponseMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleService {

  private final GoogleBusinessClient googleBusiness;
  private final GoogleResponseMapper responseMapper;
  private final ProfileBabaAsyncVendorService profileBabaAsyncVendorService;

  public List<GoogleVendor> getGoogleBusinessInformation(String query, String category, String location, String state, Integer size,
      Integer searchCategory)
      throws JsonProcessingException {
    Optional<GoogleResponse> search;
    if (query == null) {
      search = search(category, location, size);
    }else{
      search = googleBusiness.search(new Request(size, 0, query));
    }
    if (search.isPresent()) {
      List<GoogleVendor> googleVendors =
          responseMapper.responseToGoogleVendor(search.get());
      //async below
      profileBabaAsyncVendorService.saveVendors(googleVendors, searchCategory, location);
      return googleVendors;
    }
    return List.of();
  }

  private Optional<GoogleResponse> search(String category, String location, Integer size)
      throws JsonProcessingException {
    final String QUERY = "{0} in {1}";
    final int MAX_CALL = 3;
    int count = 0;
    Optional<GoogleResponse> search;
    do{
      Request request = new Request(size, 0, format(QUERY, category, location));
      search = googleBusiness.search(request);
      location = location.substring(location.indexOf(", "));
      count++;
    }while (!isGoogleResponsePresent(search) && count < MAX_CALL);
    return search;
  }

  //this method also has side effects
  private Boolean isGoogleResponsePresent(Optional<GoogleResponse> googleResponse){
    if (googleResponse.isPresent()) {
      List<Record> filtered =
          googleResponse.get().getRecords().stream()
              .filter(record -> Objects.nonNull(record.getPhone())).toList();
      googleResponse.get().setRecords(filtered);
      return !filtered.isEmpty();
    }
    return false;
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

