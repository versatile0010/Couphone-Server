package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseResponse;
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
    @Operation(summary = "브랜드 조회", description = "브랜드를 카테고리별로 조회합니다. \npath variable로 멤버 id, query string으로 카테고리 id 담아서 보내주세요!")
    public BaseResponse<List<GetBrandResponse>> getBrand(@RequestParam(required = true, value = "category-id") Long categoryId,
                                                         @PathVariable("member-id") long memberId) {
        return new BaseResponse<>(brandService.findByCategoryId(memberId, categoryId));
    }
}
