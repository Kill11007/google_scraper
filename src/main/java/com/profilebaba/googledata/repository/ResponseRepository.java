package com.profilebaba.googledata.repository;

import com.profilebaba.googledata.entity.Response;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Integer> {
  Optional<Response> findTopByOrderByIdDesc();
}
