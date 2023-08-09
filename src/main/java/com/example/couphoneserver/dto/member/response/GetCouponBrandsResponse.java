package com.example.couphoneserver.dto.member.response;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class GetCouponBrandsResponse {
    @Schema(description = "회원 정보와, 회원이 가지고 있는 쿠폰에 대한 브랜드 목록을 반환, 수정일")
    private List<BrandDto> brandInfoListByModifiedDate;

    @Schema(description = "회원 정보와, 회원이 가지고 있는 쿠폰에 대한 브랜드 목록을 반환, 쿠폰 누적")
    private List<BrandDto> brandInfoListByTotalCount;

    public GetCouponBrandsResponse(List<BrandDto> brandsInfoListByModifiedDate, List<BrandDto> brandsInfoList) {
        this.brandInfoListByModifiedDate = brandsInfoListByModifiedDate;
        this.brandInfoListByTotalCount = brandsInfoList;
    }
}
