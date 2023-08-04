package com.example.couphoneserver.dto.member.response;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class GetMemberCouponBrandsResponse {
    @Schema(example = "1", description = "회원 아이디")
    private Long id;
    @Schema(example = "김이름", description = "회원 이름")
    private String name;
    @Schema(example = "aaa@naver.com", description = "이메일")
    private String email;
    @Schema(example = "ROLE_MEMBER", description = "회원 권한")
    private MemberGrade memberGrade;
    @Schema(example = "ACTIVE", description = "회원 상태")
    private MemberStatus memberStatus;
    @Schema(description = "회원 정보와, 회원이 가지고 있는 쿠폰에 대한 브랜드 목록을 반환")
    private List<BrandDto> brandInfoList;

    public GetMemberCouponBrandsResponse(Member member, List<BrandDto> brandsInfoList) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.memberGrade = member.getGrade();
        this.memberStatus = member.getStatus();
        this.brandInfoList= brandsInfoList;
    }
}
