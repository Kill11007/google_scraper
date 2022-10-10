package com.profilebaba.googledata.mapper;

import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Record;
import com.profilebaba.googledata.service.impl.GoogleService.GoogleVendor;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GoogleResponseMapper {

  public List<GoogleVendor> responseToGoogleVendor(GoogleResponse response, Predicate<Record> recordPredicate) {
    return response.getRecords().stream()
        .filter(recordPredicate)
        .map(this::responseToGoogleVendor)
        .collect(Collectors.toList());
  }

  public GoogleVendor responseToGoogleVendor(Record record) {
    return GoogleVendor.builder()
        .name(record.getName())
        .address(record.getAddress())
        .category(record.getGoogleCategory())
        .latitude(record.getLatitude())
        .longitude(record.getLongitude())
        .phone(record.getPhone())
        .build();
  }
}
