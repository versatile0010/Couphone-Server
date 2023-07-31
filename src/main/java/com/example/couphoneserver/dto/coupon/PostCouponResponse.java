package com.example.couphoneserver.dto.coupon;

import com.example.couphoneserver.domain.entity.CouponItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCouponResponse {

    @Schema(description = "쿠폰 ID", example = "1")
    Long couponId;

    @Schema(description = "쿠폰 생성 날짜", example = "2023-07-29 18:35:46.434060")
    LocalDateTime createdDate;

    public PostCouponResponse(CouponItem couponItem) {
        couponId = couponItem.getId();
        createdDate = couponItem.getCreatedDate();
    }
}
