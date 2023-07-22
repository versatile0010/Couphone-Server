package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.CouponItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponItemRepository extends JpaRepository<CouponItem, Long> {
}
