package com.example.couphoneserver.dto.member.response;

import com.example.couphoneserver.domain.MemberGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {
    @Schema(example = "access token (jwt) ", description = "valid time = 30 min")
    private String accessToken;
    @Schema(example = "jwt")
    private String tokenType;

    @Schema(example = "1", description = "회원 아이디(PK)")
    private Long memberId;

    @Schema(example = "ROLE_MEMBER", description = "회원 권한")
    private MemberGrade grade;

    @Builder
    public LoginResponseDto(String accessToken, String tokenType, Long memberId, MemberGrade grade) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.memberId = memberId;
        this.grade = grade;
    }
}
