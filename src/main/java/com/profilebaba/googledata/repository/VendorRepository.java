package com.profilebaba.googledata.repository;

import com.profilebaba.googledata.entity.Vendor;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VendorRepository extends JpaRepository<Vendor, Long> {

  @Query("SELECT MAX(v.id) from Vendor v")
  Optional<Long> getLastAddedVendorId();

  @Query(value = "INSERT INTO google_scrape.vendors (id, name, phone, category, address, created_at, search_category_id, search_location_id, search_category, search_location, email, latitude, longitude, json_response) VALUES (:id, :name, :phone, :category, :address, :createdAt, :searchCategoryId, :searchLocationId, :searchCategory, :searchLocation, :email, :latitude, :longitude, :jsonResponse)", nativeQuery = true)
  @Modifying(clearAutomatically = true)
  void insertVendor(Long id, String name, String phone, String category, String address,
      LocalDateTime createdAt, Integer searchCategoryId, Integer searchLocationId,
      String searchCategory, String searchLocation, String email, String latitude, String longitude,
      String jsonResponse);

}
