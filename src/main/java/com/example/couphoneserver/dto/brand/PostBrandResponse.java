package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PostBrandResponse {

    @Schema(example = "1")
    private Long id;

    public PostBrandResponse(Brand brand) {
        id = brand.getId();
    }
}
