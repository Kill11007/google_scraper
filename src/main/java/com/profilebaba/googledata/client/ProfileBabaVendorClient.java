package com.profilebaba.googledata.client;

import com.profilebaba.googledata.dto.Category;
import com.profilebaba.googledata.service.impl.ProfileBabaAsyncVendorService.ProfileBabaSaveVendorRequestBody;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProfileBabaVendorClient {
  @Value("#{'${profile-baba.host}' + '${profile-baba.save-vendor}'}")
  private String saveVendorUrl;

  @Value("#{'${profile-baba.host}' + '${profile-baba.get-category-list}'}")
  private String getCategoryListUrl;
  private final RestTemplate restTemplate;

  public void saveVendors(ProfileBabaSaveVendorRequestBody body) {
    restTemplate.postForEntity(
        saveVendorUrl, body, String.class);
  }

  public CategoryData getCategories() {
    return restTemplate.getForObject(getCategoryListUrl, CategoryData.class);
  }

  @Data
  public static class CategoryData{
    private Boolean success;
    private List<Category> data;
  }
}
