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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileBabaAsyncVendorService {
  private final ProfileBabaVendorClient client;

  @Async("taskExecutor")
  public void saveVendors(List<GoogleVendor> googleVendors) {
    ProfileBabaSaveVendorRequestBody requestBody =
        new ProfileBabaSaveVendorRequestBody(getVendorFromGoogleVendor(googleVendors));
    client.saveVendors(requestBody);
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
