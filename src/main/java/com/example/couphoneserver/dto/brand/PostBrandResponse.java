package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import lombok.Getter;

@Getter
public class PostBrandResponse {

    private Long id;

    public PostBrandResponse(Brand brand) {
        id = brand.getId();
    }
}
