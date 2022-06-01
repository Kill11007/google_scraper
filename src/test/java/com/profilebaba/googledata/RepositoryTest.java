package com.profilebaba.googledata;

import static org.junit.jupiter.api.Assertions.*;

import com.profilebaba.googledata.entity.Response;
import com.profilebaba.googledata.entity.Vendor;
import com.profilebaba.googledata.enums.Status;
import com.profilebaba.googledata.repository.ResponseRepository;
import com.profilebaba.googledata.repository.VendorRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class RepositoryTest {

  @Autowired
  private VendorRepository vendorRepository;

  @Autowired
  private ResponseRepository responseRepository;

  @Test
  void test_saveAllVendors() {
    List<Vendor> vendors = List.of(
        Vendor.builder().name("vendor").email(null).category("category").searchCategory("scategry")
            .searchCategoryId(0).searchLocation("slocation").searchLocationId(8)
            .longitude("98765431").latitude("123456895").address("someaddress")
            .jsonResponse("[\"asd\"]").phone("987654321300").createdAt(LocalDateTime.now()).build());
    List<Vendor> vendorList = vendorRepository.saveAll(vendors);
    assertNotNull(vendorList);
    assertNotEquals(0, vendorList.size());

  }

  @Test
  void test_saveVendor() {
    Vendor vendor = Vendor.builder().name("vendor").searchCategory("scategry")
        .searchCategoryId(0).searchLocation("slocation").searchLocationId(8)
        .jsonResponse("[\"asd\"]").phone("987654321300").createdAt(LocalDateTime.now()).build();
    Vendor save = vendorRepository.save(vendor);
    assertNotNull(save);
    assertNotNull(save.getId());

  }

  @Test
  void test_getVendor() {
    Optional<Vendor> byId = vendorRepository.findById(83679L);

    assertNotNull(byId);
    assertTrue(byId.isPresent());
    Vendor vendor = byId.get();
    vendor.setPhone("3216549687600");
    vendorRepository.save(vendor);

  }

  @Test
  void test_saveResponse() {
    Response response = new Response();
    response.setStatus(Status.NOT_STARTED);
    response.setCategoryId(1);
    response.setLocationId(5);
    Response save = responseRepository.save(response);
    assertNotNull(save);
    assertNotNull(save.getId());

    responseRepository.delete(save);
  }
}
