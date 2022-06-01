package com.profilebaba.googledata.service.impl;

import static com.profilebaba.googledata.enums.Status.COMPLETED;
import static com.profilebaba.googledata.enums.Status.FAILED;
import static com.profilebaba.googledata.enums.Status.IN_PROGRESS;
import static com.profilebaba.googledata.service.impl.SimpleQueryService.QUERY;

import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.entity.Response;
import com.profilebaba.googledata.enums.Status;
import com.profilebaba.googledata.repository.ResponseRepository;
import com.profilebaba.googledata.service.StreamingQueryService;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@Service
public class StreamingQueryServiceImpl implements StreamingQueryService {

  @Autowired
  @Qualifier("all-categories")
  private List<Category> categories;

  @NonNull
  @Getter
  private Location location;

  @Autowired
  private ResponseRepository responseRepository;

  @Autowired
  private LocationService locationService;


  @Autowired
  private CategoryService categoryService;

  private int categoryCounter = -1;

  @Getter
  private Category category;

  private Response response;

  @Override
  public String getQuery() {
    checkIfQueryStreamStarted();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    queryStarted();
    return MessageFormat.format(QUERY, category.getName(), location.getName(),
        location.getState());
  }

  private void checkIfQueryStreamStarted() {
    if (categoryCounter < 0) {
      throw new RuntimeException("Please call hasNext query first.");
    }
  }

  @Override

  public void queryCompleted() {
    checkIfQueryInProgress();
    response.setStatus(COMPLETED);
    updateResponse();
  }

  @Override
  public void queryFailed() {
    checkIfQueryInProgress();
    this.response.setStatus(FAILED);
    updateResponse();
  }

  private void queryStarted() {
    setNextSearchLocationAndCategory();
    this.response.setStatus(IN_PROGRESS);
    updateResponse();
  }

  private void nextCategory() {
    this.category = categories.get(categoryCounter);
  }

  private void updateResponse() {
    log.info("Saving Response: {}", response);
    responseRepository.save(response);

  }

  private void checkIfQueryInProgress() {
    if (response != null && response.getStatus() != IN_PROGRESS) {
      log.info("Error Response: {}", response);
      throw new RuntimeException("Query Not Started. Please Get the query first");
    }
  }

  private void setResponse() {
    this.response = new Response();
    response.setCategoryId(this.category.getId());
    response.setLocationId(this.location.getId());
  }

  @Override
  public Boolean hasQuery() {
    categoryCounter++;
    return categoryCounter < categories.size();
  }

  private void setNextSearchLocationAndCategory() {
    if (location == null) {
      log.info("Getting next search location");
      Optional<Response> lastResponse = responseRepository.findTopByOrderByIdDesc();
      Optional<Location> nextSearchLocation = Optional.empty();
      Optional<Category> nextSearchCategory;
      if (lastResponse.isPresent()) {
        Response response = lastResponse.get();
        log.info("Response: {}", response);
        Status status = response.getStatus();
        int nextLocationId = 0;
        int nextCategoryId = -1;
        if (status == Status.IN_PROGRESS || status == Status.NOT_STARTED) {
          nextLocationId = response.getLocationId();
          nextCategoryId = response.getCategoryId();
          this.response = response;
        } else if (status == Status.COMPLETED || status == Status.FAILED) {
          nextLocationId = response.getLocationId() + 1;
        }
        nextSearchLocation = locationService.getLocation(nextLocationId);
        if (nextCategoryId != -1) {
          nextSearchCategory = categoryService.getCategory(nextCategoryId);
          if (nextSearchCategory.isPresent()) {
            this.categoryCounter = -1;
            for (Category category : categories) {
              this.categoryCounter++;
              if (Objects.equals(category.getId(), nextSearchCategory.get().getId())) {
                break;
              }
            }
          }
          this.category = nextSearchCategory.orElseThrow();
        }else{
          nextCategory();
        }
        this.location = nextSearchLocation.orElseThrow();
        if (!(status == Status.IN_PROGRESS || status == Status.NOT_STARTED)) {
          setResponse();
        }
      }
    } else {
      nextCategory();
      setResponse();
    }
    log.info("Next Location: {}", location);
    log.info("Next Category: {}", category);
  }

}
