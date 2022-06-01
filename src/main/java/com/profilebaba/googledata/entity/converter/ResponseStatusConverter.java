package com.profilebaba.googledata.entity.converter;

import com.profilebaba.googledata.enums.Status;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ResponseStatusConverter implements AttributeConverter<Status, String> {

  @Override
  public String convertToDatabaseColumn(Status status) {
    return Optional.ofNullable(status).map(Status::getStatus)
        .orElse(Status.NOT_STARTED.getStatus());
  }

  @Override
  public Status convertToEntityAttribute(String s) {
    return Status.of(s);
  }
}
