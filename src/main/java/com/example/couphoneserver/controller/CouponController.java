package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.coupon.PatchCouponCountRequest;
import com.example.couphoneserver.dto.coupon.PatchCouponCountResponse;
import com.example.couphoneserver.dto.coupon.PostCouponRequest;
import com.example.couphoneserver.dto.coupon.PostCouponResponse;
import com.example.couphoneserver.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/stamp")
    @Operation(summary = "쿠폰 스탬프 적립", description = "Request Body에 브랜드 ID, 멤버 ID 넣어주세요!")
    public BaseResponse<PatchCouponCountResponse> patchCouponItemCount(@RequestBody PatchCouponCountRequest request) {
        return new BaseResponse<>(couponService.collectStamp(request));
    }
}
