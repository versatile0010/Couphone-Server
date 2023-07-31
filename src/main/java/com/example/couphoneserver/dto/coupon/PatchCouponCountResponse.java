package com.example.couphoneserver.dto.coupon;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PatchCouponCountResponse {

    @Schema(description = "쿠폰 ID", example = "1")
    Long couponId;

    @Schema(description = "스탬프 적립 개수", example = "3")
    int stampCount;

    @Schema(description = "INACTIVE(쿠폰 적립 가능), ACTIVE(쿠폰 활성화), EXPIRED(쿠폰 만료)", example = "INACTIVE")
    CouponItemStatus couponItemStatus;

    public PatchCouponCountResponse(CouponItem couponItem) {
        couponId = couponItem.getId();
        stampCount = couponItem.getStampCount();
    }
}
