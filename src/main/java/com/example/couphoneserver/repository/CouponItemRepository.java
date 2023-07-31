package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponItemRepository extends JpaRepository<CouponItem, Long> {
    CouponItem findByMemberIdAndBrandIdAndStatus(Long mid, Long bid, CouponItemStatus status);
    CouponItem findByMemberIdAndBrandId(Long mid, Long bid);
}
