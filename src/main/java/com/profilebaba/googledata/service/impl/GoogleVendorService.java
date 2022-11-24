package com.profilebaba.googledata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.client.ProfileBabaVendorClient;
import com.profilebaba.googledata.client.ProfileBabaVendorClient.CategoryData;
import com.profilebaba.googledata.dto.Category;
import com.profilebaba.googledata.util.ApplicationUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GoogleVendorService {
  private final ProfileBabaVendorClient profileBabaVendorClient;
  private final GoogleService googleService;

  @Async("taskExecutor")
  public void saveVendorOnAllCategories(String location, Integer size) throws JsonProcessingException {
    location = ApplicationUtility.fixLocation(location);
    CategoryData categories = profileBabaVendorClient.getCategories();
    if (categories.getSuccess()) {
      List<Category> categoryList = categories.getData();
      for (Category category : categoryList) {
        if (category.getSubCatgeories().size() != 0) {
          for(Category subCategory: category.getSubCatgeories()){
            String cat = String.join(" ", subCategory.getTitle(), category.getTitle());
            //fetch and save
            googleService.getGoogleBusinessInformation(
                null, cat, location, null, size, subCategory.getId());
          }
        }else{
          String cat = category.getTitle();
          googleService.getGoogleBusinessInformation(
              null, cat, location, null, size, category.getId());
        }
      }
    }
  }
}
