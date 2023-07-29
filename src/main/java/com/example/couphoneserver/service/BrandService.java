package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.BrandException;
import com.example.couphoneserver.common.exception.CategoryException;
import com.example.couphoneserver.common.exception.DatabaseException;
import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Category;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.CategoryRepository;
import com.example.couphoneserver.repository.CouponItemRepository;
import com.example.couphoneserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final CouponItemRepository couponItemRepository;
    private final MemberRepository memberRepository;

    public PostBrandResponse saveBrand(PostBrandRequest request) {
        // 카테고리 존재하는지 검사
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

    public List<GetBrandResponse> findByCategoryId(Long mid, Long cid) {
        List<GetBrandResponse> brandList = new ArrayList<>();

        // 멤버 존재하는지 검사
        memberRepository.findById(mid)
                        .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // 카테고리 존재하는지 검사
        categoryRepository.findById(cid)
                .orElseThrow(() -> new CategoryException(CATEGORY_NOT_FOUND));

        // 카테고리로 브랜드 찾기
        List<Brand> brands = brandRepository.findAllByCategoryId(cid)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));

        for (Brand brand : brands) {
            // 쿠폰 찾기
            CouponItem couponItem = couponItemRepository.findByMemberIdAndBrandIdAndStatus(mid, brand.getId(), CouponItemStatus.ACTIVE);

            if (couponItem == null) { // 해당 브랜드에 쿠폰이 없을 경우
                brandList.add(new GetBrandResponse(brand, 0));
            } else { // 해당 브랜드에 쿠폰이 있을 경우
                brandList.add(new GetBrandResponse(brand, couponItem.getStampCount()));
            }
        }

        return brandList;
    }
}
