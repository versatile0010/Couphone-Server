package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.exception.BadRequestException;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.brand.PostBrandRequest;
import com.example.couphoneserver.dto.brand.PostBrandResponse;
import com.example.couphoneserver.dto.coupon.PostCouponRequest;
import com.example.couphoneserver.dto.coupon.PostCouponResponse;
import com.example.couphoneserver.service.BrandService;
import com.example.couphoneserver.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Tag(name = "coupons", description = "쿠폰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;

    @PostMapping("")
    @Operation(summary = "쿠폰 등록", description = "Request Body에 브랜드 ID, 멤버 ID 넣어주세요!")
    public BaseResponse<PostCouponResponse> postCouponItem(@RequestBody PostCouponRequest request){
        return new BaseResponse<>(couponService.saveCoupon(request));
    }

}
