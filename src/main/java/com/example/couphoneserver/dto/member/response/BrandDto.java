package com.example.couphoneserver.dto.member.response;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BrandDto {
    @Schema(description = "브랜드 정보")
    GetBrandResponse brandInfo;
    @Schema(description = "쿠폰 상태", example = "ACTIVE")
    CouponItemStatus couponItemStatus;
    @Schema(description = "만료 시간", example = "2024-01-29 18:35:46.434060")
    LocalDateTime expiredDate;


    @Builder
    public BrandDto(GetBrandResponse brand, CouponItemStatus status) {
        this.expiredDate = brand.getCreatedDate().plusMonths(6);
        this.couponItemStatus = status;
        this.brandInfo = brand;
    }
}
