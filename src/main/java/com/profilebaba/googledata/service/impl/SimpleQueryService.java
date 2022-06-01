package com.profilebaba.googledata.service.impl;

import static com.profilebaba.googledata.enums.Status.*;

import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.entity.Response;
import com.profilebaba.googledata.repository.ResponseRepository;
import com.profilebaba.googledata.service.QueryService;
import java.text.MessageFormat;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class SimpleQueryService implements QueryService {

  @NonNull
  private final Location location;
  @NonNull
  private final Category category;

  @Autowired
  private ResponseRepository responseRepository;

  public static final String QUERY = "{0} in {1}, {2}, IN";

  private Response response;

  public String getQuery() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    queryStarted();
    return MessageFormat.format(QUERY, category.getName(), location.getName(),
        location.getState());
  }

  public void queryCompleted() {
    checkIfQueryInProgress();
    response.setStatus(COMPLETED);
    updateResponse();
  }

  private void checkIfQueryInProgress() {
    if (response != null && response.getStatus() != IN_PROGRESS) {
      throw new RuntimeException("Query Not Started. Please Get the query first");
    }
  }

  private void setResponse() {
    this.response = new Response();
    response.setCategoryId(this.category.getId());
    response.setLocationId(this.location.getId());

  }

  public void queryFailed() {
    checkIfQueryInProgress();
    this.response.setStatus(FAILED);
    updateResponse();
  }

  private void queryStarted() {
    setResponse();
    this.response.setStatus(IN_PROGRESS);
    updateResponse();
  }

  private void updateResponse() {
    this.response = responseRepository.save(this.response);
  }

}
