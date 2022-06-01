package com.profilebaba.googledata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.client.GoogleBusinessClient;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.entity.Vendor;
import com.profilebaba.googledata.mapper.GoogleResponseMapper;
import com.profilebaba.googledata.repository.VendorRepository;
import com.profilebaba.googledata.service.StreamingQueryService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleScheduledService {

  private final ApplicationContext applicationContext;
  private final RequestService requestService;
  private final GoogleBusinessClient googleBusinessClient;
  private final GoogleResponseMapper googleResponseMapper;
  private final VendorRepository vendorRepository;

  public void fetchAndStoreGoogleSearchData() {
    StreamingQueryService streamingQueryService = applicationContext.getBean(
        StreamingQueryServiceImpl.class);
    while (streamingQueryService.hasQuery()) {
      String query = streamingQueryService.getQuery();
      Request request = requestService.getRequest(query);
      try {
        Optional<GoogleResponse> search = googleBusinessClient.search(request);
        if (search.isEmpty()) {
          streamingQueryService.queryFailed();
          continue;
        }
        List<Vendor> vendorsFromGoogleResponse = googleResponseMapper.getVendorsFromGoogleResponse(
            search.orElseThrow(),
            streamingQueryService.getCategory(), streamingQueryService.getLocation());
        log.info("Google Vendors: {}", vendorsFromGoogleResponse);
        log.info("Google Vendors Size: {}", vendorsFromGoogleResponse.size());
        vendorsFromGoogleResponse.stream().filter(Objects::nonNull)
            .map(vendorRepository::save).collect(
                Collectors.toList());
        streamingQueryService.queryCompleted();
      } catch (Exception e) {
        e.printStackTrace();
        streamingQueryService.queryFailed();
      }
    }
  }
}
