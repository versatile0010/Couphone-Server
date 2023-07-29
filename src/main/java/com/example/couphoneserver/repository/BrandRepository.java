package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
    Optional<List<Brand>> findAllByName(String name);
    Optional<List<Brand>> findAllByCategoryId(Long id);
}
