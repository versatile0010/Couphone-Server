package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.dto.category.CategoryResponse;
import com.example.couphoneserver.domain.entity.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentCategoryRepository extends JpaRepository<ParentCategory,Long>{
    List<CategoryResponse> findByName(String categoryName);
}
