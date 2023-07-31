package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.DatabaseException;
import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.coupon.PatchCouponCountRequest;
import com.example.couphoneserver.dto.coupon.PatchCouponCountResponse;
import com.example.couphoneserver.dto.coupon.PostCouponRequest;
import com.example.couphoneserver.dto.coupon.PostCouponResponse;
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

        try {
            CouponItem couponItem = request.toEntity(member, brand);
            couponItemRepository.save(couponItem);

            return new PostCouponResponse(couponItem);
        } catch (Exception e) {
            throw new DatabaseException(DATABASE_ERROR);
        }
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
