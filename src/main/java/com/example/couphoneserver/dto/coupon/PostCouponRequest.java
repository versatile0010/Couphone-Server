package com.example.couphoneserver.dto.coupon;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCouponRequest {

    @Schema(description = "멤버 ID", example = "1")
    Long memberId;

    @Schema(description = "브랜드 ID", example = "1")
    Long brandId;

    public CouponItem toEntity(Member member, Brand brand) {
        return CouponItem.builder()
                .member(member)
                .brand(brand)
                .stampCount(1)
                .status(CouponItemStatus.ACTIVE)
                .build();
    }
}
