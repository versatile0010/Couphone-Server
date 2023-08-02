package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.dto.coupon.GetCouponResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class GetBrandDetailResponse {
    @Schema(description = "브랜드 ID", example = "1")
    Long id;

    @Schema(description = "브랜드 이름", example = "메가커피")
    String name;

    @Schema(description = "보상 방법 (쿠폰 혜택)", example = "아이스 아메리카노 1잔 무료")
    String rewardDescription;

    @Schema(description = "브랜드 로고 URL", example = "----")
    String brandImageUrl;

    @Schema(description = "쿠폰 리스트")
    List<GetCouponResponse> couponList;

    public GetBrandDetailResponse(Brand brand, List<GetCouponResponse> couponList) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.rewardDescription = brand.getRewardDescription();
        this.brandImageUrl = brand.getBrandImageUrl();
        this.couponList = couponList;
    }
}
