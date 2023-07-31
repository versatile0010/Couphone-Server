package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.BrandStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBrandRequest {
    @Schema(description = "브랜드 이름", example = "메가커피")
    @NotNull
    private String name;

    @Schema(description = "보상 방법 (쿠폰 혜택)", example = "아이스 아메리카노 1잔 무료")
    @NotNull
    private String rewardDescription;

    @Schema(description = "브랜드 로고 URL", example = "----")
    @NotNull
    private String brandImageUrl;

    @Schema(description = "카테고리 ID", example = "1")
    @NotNull
    private Long categoryId;

    public Brand toEntity(Category category) {
        return Brand.builder()
                .name(name)
                .rewardDescription(rewardDescription)
                .brandImageUrl(brandImageUrl)
                .category(category)
                .status(BrandStatus.ACTIVE)
                .build();
    }
}
