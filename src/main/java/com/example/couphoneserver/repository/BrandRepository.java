package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
