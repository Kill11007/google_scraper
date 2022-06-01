package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.repository.LocationRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final LocationRepository locationRepository;

  public Optional<Location> getLocation(Integer id) {
    return locationRepository.findById(id);
  }

}
