package com.profilebaba.googledata.service;

import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;

public interface StreamingQueryService extends QueryService{

  Boolean hasQuery();

  Category getCategory();

  Location getLocation();

}
