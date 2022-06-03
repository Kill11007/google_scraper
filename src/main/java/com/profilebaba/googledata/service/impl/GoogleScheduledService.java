package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.client.GoogleBusinessClient;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import com.profilebaba.googledata.entity.Vendor;
import com.profilebaba.googledata.enums.VendorHeader;
import com.profilebaba.googledata.mapper.GoogleResponseMapper;
import com.profilebaba.googledata.service.StreamingQueryService;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
  private final VendorService vendorService;
  private final CsvVendorService csvVendorService;

  public void fetchAndStoreGoogleSearchData() throws IOException {
    StreamingQueryService streamingQueryService = applicationContext.getBean(
        StreamingQueryServiceImpl.class);
    long lastAddedVendorId = 0l;
    FileWriter out = csvVendorService.createCSVFile();
    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(VendorHeader.class))) {
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

          log.info("Google Vendors Size: {}", vendorsFromGoogleResponse.size());
          for (Vendor vendor : vendorsFromGoogleResponse) {
            if (Objects.nonNull(vendor)) {
              vendor.setId(++lastAddedVendorId);
              vendorService.insertCSV(vendor, printer);
            }
          }
          streamingQueryService.queryCompleted();
        } catch (Exception e) {
          e.printStackTrace();
          streamingQueryService.queryFailed();
        }
      }
    }

  }

}
