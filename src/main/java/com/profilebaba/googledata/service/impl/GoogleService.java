package com.profilebaba.googledata.service.impl;

import static java.text.MessageFormat.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.client.GoogleBusinessClient;
import com.profilebaba.googledata.client.ProfileBabaVendorClient;
import com.profilebaba.googledata.dto.GoogleResponse;
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

  public List<GoogleVendor> getGoogleBusinessInformation(String category, String location, String state, Integer size)
      throws JsonProcessingException {
    final String QUERY = "{0} in {1}";
    Request request = new Request(size, 0, format(QUERY, category, location));
    Optional<GoogleResponse> search = googleBusiness.search(request);
    if (search.isPresent()) {
      List<GoogleVendor> googleVendors =
          responseMapper.responseToGoogleVendor(
              search.get(), record -> Objects.nonNull(record.getPhone()));
      //async below
      profileBabaAsyncVendorService.saveVendors(googleVendors);
      return googleVendors;
    }
    return null;
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

