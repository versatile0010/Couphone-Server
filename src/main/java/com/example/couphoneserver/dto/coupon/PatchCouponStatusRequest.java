package com.example.couphoneserver.dto.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchCouponStatusRequest {

    @Schema(description = "쿠폰 ID", example = "1")
    Long couponId;

}
