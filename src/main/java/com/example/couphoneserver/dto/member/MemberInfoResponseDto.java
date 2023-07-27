package com.example.couphoneserver.dto.member;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {
    @Schema(example = "1", description = "회원 아이디")
    private Long id;
    @Schema(example = "김이름", description = "회원 이름")
    private String name;
    @Schema(example = "010-1111-1111", description = "전화번호")
    private String phoneNumber;
    @Schema(example = "ROLE_MEMBER", description = "회원 권한")
    private MemberGrade memberGrade;
    @Schema(example = "ACTIVE", description = "회원 상태")
    private MemberStatus memberStatus;

    public MemberInfoResponseDto(Member member) {
        id = member.getId();
        name = member.getName();
        phoneNumber = member.getPhoneNumber();
        memberGrade = member.getGrade();
        memberStatus = member.getStatus();
    }
}
