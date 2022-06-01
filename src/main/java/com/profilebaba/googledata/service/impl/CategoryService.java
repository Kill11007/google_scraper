package com.profilebaba.googledata.service.impl;

import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.repository.CategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public Optional<Category> getCategory(Integer id) {
    return categoryRepository.findById(id);
  }
}
