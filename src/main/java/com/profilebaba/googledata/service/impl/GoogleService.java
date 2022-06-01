package com.profilebaba.googledata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.profilebaba.googledata.client.GoogleBusinessClient;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.entity.Vendor;
import com.profilebaba.googledata.mapper.GoogleResponseMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleService {

  private final GoogleBusinessClient googleBusiness;
  private final LocationService locationService;
  private final CategoryService categoryService;
  private final ApplicationContext applicationContext;
  private final RequestService requestService;
  private final GoogleResponseMapper responseMapper;

  public List<Vendor> getGoogleBusinessInformation(Integer categoryId, Integer locationId)
      throws JsonProcessingException {
    Optional<Category> category = categoryService.getCategory(categoryId);
    Optional<Location> location = locationService.getLocation(locationId);
    SimpleQueryService queryService = applicationContext.getBean(SimpleQueryService.class,
        location.orElseThrow(),
        category.orElseThrow());
    String query = queryService.getQuery();
    Request request = requestService.getRequest(query);
    Optional<GoogleResponse> search = googleBusiness.search(request);
    if (search.isEmpty()) {
      queryService.queryFailed();
    }else {
      queryService.queryCompleted();
    }

    List<Vendor> vendorsFromGoogleResponse = responseMapper.getVendorsFromGoogleResponse(
        search.orElseThrow(), category.get(),
        location.get());

    return vendorsFromGoogleResponse;
  }


}
