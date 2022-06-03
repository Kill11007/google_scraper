package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.entity.Vendor;
import com.profilebaba.googledata.repository.VendorRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorService {

  private final VendorRepository vendorRepository;

  public void insert(Vendor vendor) {
    vendorRepository.insertVendor(vendor.getId(), vendor.getName(), vendor.getPhone(),
        vendor.getCategory(), vendor.getAddress(), vendor.getCreatedAt(),
        vendor.getSearchCategoryId(), vendor.getSearchLocationId(), vendor.getSearchCategory(),
        vendor.getSearchLocation(), vendor.getEmail(), vendor.getLatitude(), vendor.getLongitude(),
        vendor.getJsonResponse());
  }

  public void insertCSV(Vendor vendor, CSVPrinter printer) throws IOException {
    printer.printRecord(vendor.getId(), vendor.getName(), vendor.getPhone(),
        vendor.getCategory(), vendor.getAddress(), vendor.getCreatedAt(),
        vendor.getSearchCategoryId(), vendor.getSearchLocationId(), vendor.getSearchCategory(),
        vendor.getSearchLocation(), vendor.getEmail(), vendor.getLatitude(), vendor.getLongitude(),
        vendor.getJsonResponse());
  }

  public Optional<Long> getLastAddedVendorId() {
    return vendorRepository.getLastAddedVendorId();
  }
}
