package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.dto.Request;
import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.service.impl.SimpleQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

  @Value("${request.total-records:500}")
  private Integer records;

  public Request getRequest(String query) {
    return new Request(records, 0, query);
  }

  public Request getRequest(Integer totalRecords, Integer offset, String query) {
    return new Request(totalRecords, offset, query);
  }

}
