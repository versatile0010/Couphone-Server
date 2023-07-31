package com.example.couphoneserver.dto.coupon;

import com.example.couphoneserver.domain.entity.CouponItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PatchCouponCountResponse {

    @Schema(description = "쿠폰 ID", example = "1")
    Long couponId;

    @Schema(description = "스탬프 적립 개수", example = "3")
    int stampCount;

    public PatchCouponCountResponse(CouponItem couponItem) {
        couponId = couponItem.getId();
        stampCount = couponItem.getStampCount();
    }
}
