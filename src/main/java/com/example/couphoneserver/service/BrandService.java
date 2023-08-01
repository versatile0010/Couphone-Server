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
        Category category = findCategoryById(request.getCategoryId());

        // 브랜드 이름 중복 검사
        existsBrandByName(request.getName());


        // 브랜드 저장
        Brand brand = brandRepository.save(request.toEntity(category));

        if (brand == null) {
            throw new DatabaseException(DATABASE_ERROR);
        }

        return new PostBrandResponse(brand);
    }

    public List<GetBrandResponse> findByCategoryId(Long mid, Long cid) {
        List<GetBrandResponse> brandList = new ArrayList<>();

        // 멤버 존재하는지 검사
        findMemberById(mid);

        // 카테고리 존재하는지 검사
        findCategoryById(cid);

        // 카테고리로 브랜드 찾기
        List<Brand> brands = brandRepository.findAllByCategoryId(cid)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));

        for (Brand brand : brands) {
            // 쿠폰 찾기
            CouponItem couponItem = couponItemRepository.findByMemberIdAndBrandIdAndStatusNotExpired(mid, brand.getId());

            if (couponItem == null) { // 해당 브랜드에 쿠폰이 없을 경우
                brandList.add(new GetBrandResponse(brand, 0));
            } else { // 해당 브랜드에 쿠폰이 있을 경우
                brandList.add(new GetBrandResponse(brand, couponItem.getStampCount()));
            }
        }

        return brandList;
    }

    public List<GetBrandResponse> findByNameContaining(Long mid, String name) {
        List<GetBrandResponse> brandList = new ArrayList<>();

        // 멤버 존재하는지 검사
        findMemberById(mid);

        // 검색어로 브랜드 찾기
        List<Brand> brands = brandRepository.findAllByNameContaining(name)
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

    public GetBrandDetailResponse getBrandDetail(Long bid, Long mid) {
        // 멤버 존재하는지 검사
        findMemberById(mid);

        // 브랜드 존재하는지 검사
        Brand brand = findById(bid);

        // 쿠폰 찾기
        List<CouponItem> couponItems = couponItemRepository.findAllByMemberIdAndBrandId(mid, bid);
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

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private void existsBrandByName(String name) {
        if (brandRepository.existsByName(name)) {
            throw new BrandException(DUPLICATE_BRAND_NAME);
        }
    }
}
