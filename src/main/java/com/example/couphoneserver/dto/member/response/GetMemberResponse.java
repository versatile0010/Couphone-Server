package com.example.couphoneserver.dto.member.response;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetMemberResponse {
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

    public GetMemberResponse(Member member) {
        id = member.getId();
        name = member.getName();
        email = member.getEmail();
        memberGrade = member.getGrade();
        memberStatus = member.getStatus();
    }
}
