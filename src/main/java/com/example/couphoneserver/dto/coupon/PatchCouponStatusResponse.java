package com.example.couphoneserver.dto.coupon;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PatchCouponStatusResponse {

    @Schema(description = "쿠폰 ID", example = "1")
    Long couponId;

    @Schema(description = "INACTIVE(쿠폰 적립 가능), ACTIVE(쿠폰 활성화), EXPIRED(쿠폰 만료)", example = "INACTIVE")
    CouponItemStatus couponItemStatus;

    public PatchCouponStatusResponse(CouponItem couponItem) {
        couponId = couponItem.getId();
        couponItemStatus = couponItem.getStatus();
    }
}
