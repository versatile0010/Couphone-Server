package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.*;
import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Category;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.brand.GetBrandDetailResponse;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.dto.coupon.GetCouponResponse;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.CategoryRepository;
import com.example.couphoneserver.repository.CouponItemRepository;
import com.example.couphoneserver.repository.MemberRepository;
import com.example.couphoneserver.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final CouponItemRepository couponItemRepository;
    private final MemberRepository memberRepository;

    public PostBrandResponse saveBrand(PostBrandRequest request, String brandImageUrl) {
        // 카테고리 존재하는지 검사
        Category category = findCategoryById(request.getCategoryId());

        // 브랜드 이름 중복 검사
        existsBrandByName(request.getName());

        // 브랜드 저장
        Brand brand = brandRepository.save(request.toEntity(category, brandImageUrl));

        if (brand == null) {
            throw new DatabaseException(DATABASE_ERROR);
        }

        return new PostBrandResponse(brand);
    }

    public List<GetBrandResponse> findByCategoryId(Long memberId, Long categoryId, int sortedBy) {
        List<Brand> brands;

        // 카테고리 존재하는지 검사
        findCategoryById(categoryId);

        // 카테고리로 브랜드 찾기
        if (sortedBy == 3) {
            brands = brandRepository.findAllByCategoryIdOrderByName(categoryId)
                    .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));
        } else {
            brands = brandRepository.findAllByCategoryId(categoryId)
                    .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));
        }

        return getNotExpiredBrandList(memberId, brands, sortedBy);
    }

    public List<GetBrandResponse> findByNameContaining(Long memberId, String name, int sortedBy) {

        // 검색어로 브랜드 찾기
        List<Brand> brands = brandRepository.findAllByNameContaining(name)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));

        return getNotExpiredBrandList(memberId, brands, sortedBy);
    }

    public GetBrandDetailResponse getBrandDetail(Long brandId, Long memberId) {

        // 브랜드 존재하는지 검사
        Brand brand = findById(brandId);

        // 쿠폰 찾기
        List<CouponItem> couponItems = couponItemRepository.findAllByMemberIdAndBrandId(memberId, brandId);
        List<GetCouponResponse> getCoupons = new ArrayList<>();
        for (CouponItem couponItem: couponItems) {
            getCoupons.add(new GetCouponResponse(couponItem));
        }

        return new GetBrandDetailResponse(brand, getCoupons);
    }

    private Brand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CATEGORY_NOT_FOUND));
    }

    private void existsBrandByName(String name) {
        if (brandRepository.existsByName(name)) {
            throw new BrandException(DUPLICATE_BRAND_NAME);
        }
    }


    private List<GetBrandResponse> getNotExpiredBrandList(Long memberId, List<Brand> brands, int sortedBy) {
        List<GetBrandResponse> brandList = new ArrayList<>();
        List<CouponItem> couponItems;
        List<Long> brandIdx = new ArrayList<>();

        System.out.println(sortedBy);

        if (sortedBy == 1) {
            // 쿠폰 찾기
            couponItems = couponItemRepository.findByMemberIdOrderByStampCountAndCreatedDate(memberId);

            // 사용자가 쿠폰을 가지고 있는 브랜드 먼저 추가
            for (CouponItem couponItem: couponItems) {
                brandIdx.add(couponItem.getBrand().getId());
                brandList.add(new GetBrandResponse(couponItem.getBrand(), couponItem.getStampCount()));
            }

            // 나머지 브랜드 추가
            for (Brand brand : brands) {
                if (brandIdx.contains(brand.getId())) {
                    continue;
                }

                brandList.add(new GetBrandResponse(brand, 0));
            }
        } else if (sortedBy == 2) {
            // 쿠폰 찾기
            couponItems = couponItemRepository.findByMemberIdOrderByCreatedDateAndStampCount(memberId);

            // 사용자가 쿠폰을 가지고 있는 브랜드 먼저 추가
            for (CouponItem couponItem: couponItems) {
                brandIdx.add(couponItem.getBrand().getId());
                brandList.add(new GetBrandResponse(couponItem.getBrand(), couponItem.getStampCount()));
            }

            // 나머지 브랜드 추가
            for (Brand brand : brands) {
                if (brandIdx.contains(brand.getId())) {
                    continue;
                }

                brandList.add(new GetBrandResponse(brand, 0));
            }
        } else {
            for (Brand brand : brands) {
                // 쿠폰 찾기
                CouponItem couponItem = couponItemRepository.findByMemberIdAndBrandIdAndStatusNotExpired(memberId, brand.getId());

                if (couponItem == null) {
                    brandList.add(new GetBrandResponse(brand, 0));
                } else {
                    brandList.add(new GetBrandResponse(brand, couponItem.getStampCount()));
                }
            }
        }

        return brandList;
    }
}
