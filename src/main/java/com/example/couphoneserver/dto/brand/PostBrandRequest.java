package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class PostBrandRequest {
    private String name;
    private String rewardDescription;
    private String brandImageUrl;

    public Brand toEntity() {
        return Brand.builder()
                .name(name)
                .rewardDescription(rewardDescription)
                .brandImageUrl(brandImageUrl)
                .build();
    }
}
