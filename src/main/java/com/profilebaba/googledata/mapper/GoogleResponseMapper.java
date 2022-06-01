package com.profilebaba.googledata.mapper;

import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Record;
import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.entity.Vendor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GoogleResponseMapper {

  public Vendor responseToVendor(Record record, Category category, Location location) {
    Vendor vendor = new Vendor();
    vendor.setName(record.getName());
    vendor.setCategory(record.getGoogleCategory());
    vendor.setAddress(record.getAddress());
    vendor.setLatitude(record.getLatitude());
    vendor.setLongitude(record.getLongitude());
    vendor.setCreatedAt(LocalDateTime.now());
    vendor.setSearchCategory(category.getName());
    vendor.setSearchCategoryId(category.getId());
    vendor.setSearchLocation(String.format("%s, %s", location.getName(), location.getState()));
    vendor.setSearchLocationId(location.getId());
    vendor.setPhone(record.getPhone());
    vendor.setJsonResponse(record.getJsonResponse());
    return vendor;
  }

  public List<Vendor> getVendorsFromGoogleResponse(GoogleResponse response, Category category,
      Location location) {
    return response.getRecords().stream()
        .map(record -> responseToVendor(record, category, location))
        .collect(
            Collectors.toList());
  }

}
