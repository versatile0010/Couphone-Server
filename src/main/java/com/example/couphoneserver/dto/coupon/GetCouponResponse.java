package com.example.couphoneserver.dto.coupon;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCouponResponse {
    @Schema(description = "쿠폰 ID", example = "1")
    Long id;

    @Schema(description = "스탬프 적립 개수", example = "3")
    int stampCount;

    @Schema(description = "INACTIVE(쿠폰 적립 가능), ACTIVE(쿠폰 활성화), EXPIRED(쿠폰 만료)", example = "INACTIVE")
    CouponItemStatus status;


    @Schema(description = "생성 시간", example = "2023-07-29 18:35:46.434060")
    LocalDateTime createdDate;

    @Schema(description = "수정 시간", example = "2023-07-29 18:35:46.434060")
    LocalDateTime modifiedDate;

    public GetCouponResponse(CouponItem couponItem) {
        this.id = couponItem.getId();
        this.stampCount = couponItem.getStampCount();
        this.status = couponItem.getStatus();
        this.createdDate = couponItem.getCreatedDate();
        this.modifiedDate = couponItem.getModifiedDate();
    }
}