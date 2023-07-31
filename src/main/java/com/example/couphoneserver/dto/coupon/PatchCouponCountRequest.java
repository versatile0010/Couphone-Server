package com.example.couphoneserver.dto.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchCouponCountRequest {

    @Schema(description = "멤버 ID", example = "1")
    Long memberId;

    @Schema(description = "브랜드 ID", example = "1")
    Long brandId;

}
