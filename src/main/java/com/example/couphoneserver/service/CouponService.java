package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.CouponException;
import com.example.couphoneserver.common.exception.DatabaseException;
import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.coupon.*;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.CouponItemRepository;
import com.example.couphoneserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.*;

/**
 * 쿠폰 관련 비지니스 로직
 */

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final CouponItemRepository couponItemRepository;
    private final BrandRepository brandRepository;

    public PostCouponResponse saveCoupon(PostCouponRequest request) {
        // 멤버 존재하는지 검사
        Member member = findMemberById(request.getMemberId());

        // 브랜드 존재하는지 검사
        Brand brand = findBrandById(request.getBrandId());

        // 적립 가능한 쿠폰이 있을 경우
        if (couponItemRepository.findByMemberIdAndBrandIdAndStatus(request.getMemberId(), request.getBrandId(), CouponItemStatus.INACTIVE) != null) {
            throw new CouponException(DUPLICATE_COUPON_INACTIVE);

        }

        // 쿠폰 생성
        CouponItem couponItem = request.toEntity(member, brand);

        if (couponItemRepository.save(couponItem) == null) {
            throw new DatabaseException(DATABASE_ERROR);
        }

        return new PostCouponResponse(couponItem);
    }

    public PatchCouponCountResponse collectStamp(Long couponId) {
        // 쿠폰 찾기
        CouponItem couponItem = couponItemRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(COUPON_NOT_FOUND));

        // 해당 쿠폰을 적립할 수 없는 상태일 경우
        if (couponItem.getStatus() != CouponItemStatus.INACTIVE) {
            throw new CouponException(COUPON_NOT_COLLECT);
        }

        // 쿠폰 스탬프 추가
        couponItem.collectStamp();

        if (couponItemRepository.save(couponItem) == null) {
            throw new DatabaseException(DATABASE_ERROR);
        }

        // 예외 처리,,, 해야 함

        return new PatchCouponCountResponse(couponItem);
    }

    public PatchCouponStatusResponse useCoupon(Long couponId) {
        // 쿠폰 찾기
        CouponItem couponItem = couponItemRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(COUPON_NOT_FOUND));

        // 해당 쿠폰을 사용할 수 없는 상태일 경우
        if (couponItem.getStatus() != CouponItemStatus.ACTIVE) {
            throw new CouponException(COUPON_NOT_ACTIVE);
        }

        // 쿠폰 사용하기
        couponItem.setStatus(CouponItemStatus.EXPIRED);

        if (couponItemRepository.save(couponItem) == null) {
            throw new DatabaseException(DATABASE_ERROR);
        }

        return new PatchCouponStatusResponse(couponItem);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Brand findBrandById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));
    }
}
