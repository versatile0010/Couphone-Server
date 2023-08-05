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

import java.security.Principal;
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

    private final MemberService memberService;

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

    public List<GetBrandResponse> findByCategoryId(Principal principal, Long categoryId) {

        // 멤버 ID
        Long memberId = findMemberIdByPrincipal(principal);

        // 멤버 존재하는지 검사
        findMemberById(memberId);

        // 카테고리 존재하는지 검사
        findCategoryById(categoryId);

        // 카테고리로 브랜드 찾기
        List<Brand> brands = brandRepository.findAllByCategoryId(categoryId)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));

        return getNotExpiredBrandList(memberId, brands);
    }

    public List<GetBrandResponse> findByNameContaining(Principal principal, String name) {

        // 멤버 ID
        Long memberId = findMemberIdByPrincipal(principal);

        // 멤버 존재하는지 검사
        findMemberById(memberId);

        // 검색어로 브랜드 찾기
        List<Brand> brands = brandRepository.findAllByNameContaining(name)
                .orElseThrow(() -> new BrandException(BRAND_NOT_FOUND));

        return getNotExpiredBrandList(memberId, brands);
    }

    public GetBrandDetailResponse getBrandDetail(Long brandId, Principal principal) {
        // 멤버 ID
        Long memberId = findMemberIdByPrincipal(principal);

        // 멤버 존재하는지 검사
        findMemberById(memberId);

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

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private void existsBrandByName(String name) {
        if (brandRepository.existsByName(name)) {
            throw new BrandException(DUPLICATE_BRAND_NAME);
        }
    }

    private Long findMemberIdByPrincipal(Principal principal) {
        String email = principal.getName();
        return memberService.findOneByEmail(email).getId();
    }

    private List<GetBrandResponse> getNotExpiredBrandList(Long memberId, List<Brand> brands) {
        List<GetBrandResponse> brandList = new ArrayList<>();

        for (Brand brand : brands) {
            // 쿠폰 찾기

            CouponItem couponItem = couponItemRepository.findByMemberIdAndBrandIdAndStatusNotExpired(memberId, brand.getId());

            if (couponItem == null) { // 해당 브랜드에 쿠폰이 없을 경우
                brandList.add(new GetBrandResponse(brand, 0));
            } else { // 해당 브랜드에 쿠폰이 있을 경우
                brandList.add(new GetBrandResponse(brand, couponItem.getStampCount()));
            }
        }

        return brandList;
    }
}
