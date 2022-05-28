package com.profilebaba.googledata.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "vendors")
public class Vendor {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  private String phone;

  private String category;

  private String location;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "search_category_id")
  private Integer searchCategoryId;

  @Column(name = "search_location_id")
  private Integer searchLocationId;

  @Column(name = "search_category")
  private String searchCategory;

  @Column(name = "search_location")
  private String searchLocation;

  private String email;

  private String latitude;
  private String longitude;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Vendor vendor = (Vendor) o;
    return Objects.equals(id, vendor.id);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
