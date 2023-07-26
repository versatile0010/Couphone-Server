package com.example.couphoneserver.dto.member;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberResponse {
    @Schema(example = "1", description = "회원 아이디")
    private Long id;
    @Schema(example = "김이름", description = "회원 이름")
    private String name;
    @Schema(example = "010-1234-5678", description = "휴대폰 번호")
    private String phoneNumber;
    @Schema(example = "ROLE_MEMBER", description = "회원 권한")
    private MemberGrade memberGrade;

    public AddMemberResponse(Member member) {
        id = member.getId();
        name = member.getName();
        phoneNumber = member.getPhoneNumber();
        memberGrade = member.getGrade();
    }
}
