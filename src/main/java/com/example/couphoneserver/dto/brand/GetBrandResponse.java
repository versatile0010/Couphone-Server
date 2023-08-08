package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetBrandResponse {
    @Schema(description = "브랜드 ID", example = "1")
    Long id;

    @Schema(description = "브랜드 이름", example = "메가커피")
    String name;

    @Schema(description = "보상 방법 (쿠폰 혜택)", example = "아이스 아메리카노 1잔 무료")
    String rewardDescription;

    @Schema(description = "브랜드 로고 URL", example = "----")
    String brandImageUrl;

    @Schema(description = "스탬프 적립 개수", example = "3")
    int stampCount;

    @Schema(description = "쿠폰 생성 시간", example = "2023-07-29 18:35:46.434060")
    LocalDateTime createdDate;

    public GetBrandResponse(Brand brand, int stampCount, LocalDateTime createdDate) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.rewardDescription = brand.getRewardDescription();
        this.brandImageUrl = brand.getBrandImageUrl();
        this.stampCount = stampCount;
        this.createdDate = createdDate;
    }
}
