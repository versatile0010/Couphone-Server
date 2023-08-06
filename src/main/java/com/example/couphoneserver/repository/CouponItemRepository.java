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
    "and c.status <> 'EXPIRED' " +
    "ORDER BY c.stampCount DESC, c.createdDate ASC")
    CouponItem findByMemberIdAndBrandIdAndStatusNotExpired(@Param("mid") Long mid, @Param("bid") Long bid);

    List<CouponItem> findAllByMemberIdAndBrandId(Long mid, Long bid);

    Optional<CouponItem> findById(Long couponId);

    // 특정 회원이 가지고 있는 유효한 쿠폰을 1. 도장 개수 많은 순, 2. 생성시간이 빠른 순으로 조회.
    @Query("SELECT c FROM CouponItem c WHERE c.member.id = :memberId AND c.status <> 'EXPIRED' ORDER BY c.stampCount DESC, c.createdDate ASC")
    List<CouponItem> findByMemberIdOrderByStampCountAndCreatedDate(@Param("memberId") Long memberId);

    // 특정 회원이 가지고 있는 유효한 쿠폰을 1. 생성시간이 빠른 순, 2. 도장 개수가 많은 순으로 조회.
    @Query("SELECT c FROM CouponItem c WHERE c.member.id = :memberId AND c.status <> 'EXPIRED' ORDER BY c.createdDate, c.stampCount DESC")
    List<CouponItem> findByMemberIdOrderByCreatedDateAndStampCount(@Param("memberId") Long memberId);

    // 특정 회원이 가지고 있는 유효한 쿠폰을 브랜드 이름으로 조회
    @Query("SELECT c FROM CouponItem c WHERE c.member.id = :memberId AND c.status <> 'EXPIRED' ORDER BY c.brand.name")
    List<CouponItem> findByMemberIdOrderByBrandName(@Param("memberId") Long memberId);
}
