package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.client.ProfileBabaVendorClient;
import com.profilebaba.googledata.service.impl.GoogleService.GoogleVendor;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProfileBabaAsyncVendorService {
  private final ProfileBabaVendorClient client;

  @Async("taskExecutor")
  public void saveVendors(List<GoogleVendor> googleVendors, Integer searchCategory, String location) {
    log.info("saveVendors method started");
    ProfileBabaSaveVendorRequestBody requestBody =
        new ProfileBabaSaveVendorRequestBody(getVendorFromGoogleVendor(googleVendors), searchCategory, location);
    client.saveVendors(requestBody);
    log.info("vendors saved");
  }

  private List<Vendor> getVendorFromGoogleVendor(List<GoogleVendor> googleVendor){
    return googleVendor.stream().map(this::from).collect(Collectors.toList());
  }

  private Vendor from(GoogleVendor vendor) {
    return Vendor.builder()
        .address(vendor.getAddress())
        .category(vendor.getCategory())
        .name(vendor.getName())
        .lat(vendor.getLatitude())
        .lng(vendor.getLongitude())
        .phone(vendor.getPhone()).build();
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProfileBabaSaveVendorRequestBody {
    private List<Vendor> vendor;
    private Integer cat;
    private String location;
  }
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  private static class Vendor{
    private String name;
    private String phone;
    private String category;
    private String address;
    private String lat;
    private String lng;
  }
}
