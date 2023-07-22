package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class PostBrandRequest {
    @Schema(example = "메가커피")
    private String name;
    @Schema(example = "아이스 아메리카노 1잔 무료")
    private String rewardDescription;
    @Schema(example = "----")
    private String brandImageUrl;

    public Brand toEntity() {
        return Brand.builder()
                .name(name)
                .rewardDescription(rewardDescription)
                .brandImageUrl(brandImageUrl)
                .build();
    }
}
