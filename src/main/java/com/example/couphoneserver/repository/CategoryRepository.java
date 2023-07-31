package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Category;
import com.example.couphoneserver.repository.mapping.CategoryInfoMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    List<Category> findByNameLike(String query);
}
