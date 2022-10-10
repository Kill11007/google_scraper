package com.profilebaba.googledata.client;

import com.profilebaba.googledata.service.impl.ProfileBabaAsyncVendorService.ProfileBabaSaveVendorRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProfileBabaVendorClient {
  @Value("#{'${profilebaba.save-vendor.host}' + '${profilebaba.save-vendor.endpoint}'}")
  private String saveVendorUrl;
  private final RestTemplate restTemplate;

  public void saveVendors(ProfileBabaSaveVendorRequestBody body) {
    restTemplate.postForEntity(
        saveVendorUrl, body, String.class);
  }

}
