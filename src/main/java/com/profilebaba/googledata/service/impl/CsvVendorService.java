package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.entity.Response;
import com.profilebaba.googledata.enums.Status;
import com.profilebaba.googledata.repository.ResponseRepository;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvVendorService {

  private final ResponseRepository responseRepository;

  public FileWriter createCSVFile() throws IOException {
    Optional<Response> topByOrderByIdDesc = responseRepository.findTopByOrderByIdDesc();
    Response response = topByOrderByIdDesc.orElseThrow();
    int locationId = response.getLocationId();
    int categoryId = response.getCategoryId();
    if (response.getStatus() == Status.COMPLETED || response.getStatus() == Status.FAILED) {
      locationId = locationId + 1;
      categoryId = -1;
    }
    String filename = MessageFormat.format("Vendor_{0}_{1}.csv", locationId, categoryId);
    File file = new File(filename);
    if (file.exists()) {
      file.delete();
    }
    FileWriter out = new FileWriter(filename);
    return out;
  }

}
