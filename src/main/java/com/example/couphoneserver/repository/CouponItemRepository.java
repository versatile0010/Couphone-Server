package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponItemRepository extends JpaRepository<CouponItem, Long> {
    CouponItem findByMemberIdAndBrandIdAndStatus(Long mid, Long bid, CouponItemStatus status);

    @Query("select c from CouponItem c " +
    "where c.brand.id = :bid and c.member.id = :mid " +
    "and c.status <> 'EXPIRED'")
    CouponItem findByMemberIdAndBrandIdAndStatusNotExpired(@Param("mid") Long mid, @Param("bid") Long bid);

    List<CouponItem> findAllByMemberIdAndBrandId(Long mid, Long bid);

    Optional<CouponItem> findById(Long couponId);
}
