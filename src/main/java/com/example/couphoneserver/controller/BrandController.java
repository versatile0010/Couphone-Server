package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.exception.BadRequestException;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.brand.GetBrandDetailResponse;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Tag(name = "brands", description = "브랜드 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @PostMapping("")
    @Operation(summary = "브랜드 등록", description = "Request Body에 브랜드 이름, 보상 설명, 이미지 url, 카테고리 id 담아서 보내주세요!")
    public BaseResponse<PostBrandResponse> postBrand(@RequestBody PostBrandRequest request){
        return new BaseResponse<>(brandService.saveBrand(request));
    }

    @GetMapping("/{member-id}")
    @Operation(summary = "브랜드 조회",
            description = "브랜드를 검색어 또는 카테고리별로 조회합니다. Path Variable로 멤버 ID 담아서 보내주세요! " +
            "Query String으로 카테고리 ID를 담아서 보내주시면 카테고리별로 브랜드를 조회하고, " +
            "검색어 담아서 보내주시면 검색한 이름에 따라 브랜드를 조회합니다.")
    public BaseResponse<List<GetBrandResponse>> getBrand(@RequestParam(required = false, value = "category-id") Long categoryId,
                                                                     @RequestParam(required = false, value = "name") String name,
                                                                     @PathVariable("member-id") Long memberId) {

        if ((categoryId != null) && (name == null)) {
            return new BaseResponse<>(brandService.findByCategoryId(memberId, categoryId));
        }

        if ((categoryId == null) && (name != null)) {
            return new BaseResponse<>(brandService.findByNameContaining(memberId, name));
        }

        throw new BadRequestException(BAD_REQUEST);
    }

    @GetMapping("/detail")
    @Operation(summary = "브랜드 상세 조회",
            description = "브랜드를 상세 조회합니다. Query String으로 멤버 ID, 브랜드 ID 담아서 보내주세요!")
    public BaseResponse<GetBrandDetailResponse> getBrandDetail(@RequestParam(required = true, value = "member-id") Long memberId,
                                                         @RequestParam(required = true, value = "brand-id") Long brandId) {

        return new BaseResponse<>(brandService.getBrandDetail(brandId, memberId));
    }
}
