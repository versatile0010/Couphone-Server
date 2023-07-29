package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.CategoryException;
import com.example.couphoneserver.common.exception.DatabaseException;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Category;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public PostBrandResponse saveBrand(PostBrandRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryException(CATEGORY_NOT_FOUND));

        // 브랜드 이름 중복 검사
        if (brandRepository.existsByName(request.getName())) {
            throw new BrandException(DUPLICATE_BRAND_NAME);
        }

        try {
            Brand brand = request.toEntity(category);
            brandRepository.save(brand);

            return new PostBrandResponse(brand);
        } catch (Exception e) {
            throw new DatabaseException(DATABASE_ERROR);
        }
    }
}
