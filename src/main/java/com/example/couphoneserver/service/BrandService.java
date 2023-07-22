package com.example.couphoneserver.service;

import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public PostBrandResponse saveBrand(PostBrandRequest request) {
        Brand brand = request.toEntity();
        brandRepository.save(brand);

        return new PostBrandResponse(brand);
    }
}
