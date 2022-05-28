package com.profilebaba.googledata.entity;

import com.profilebaba.googledata.entity.converter.ResponseStatusConverter;
import com.profilebaba.googledata.enums.Status;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Response {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "category_id")
  private Integer categoryId;

  @Column(name = "location_id")
  private Integer locationId;

  @Convert(converter = ResponseStatusConverter.class)
  private Status status;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Response response = (Response) o;
    return Objects.equals(id, response.id);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
