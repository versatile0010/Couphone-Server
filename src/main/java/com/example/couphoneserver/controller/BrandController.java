package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @PostMapping("")
    @Operation(summary = "브랜드 등록", description = "Request Body에 브랜드 이름, 보상 설명, 이미지 url 담아서 보내주세요!")
    public BaseResponse<PostBrandResponse> postBrand(@RequestBody PostBrandRequest request){
        return new BaseResponse<>(brandService.saveBrand(request));
    }
}
