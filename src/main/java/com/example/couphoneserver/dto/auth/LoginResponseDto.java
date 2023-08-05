package com.example.couphoneserver.dto.auth;

import com.example.couphoneserver.domain.MemberGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjMTFjMjJjQG5hdmVyLmNvbSIsImF1dGgiOiJST0xFX01FTUJFUixST0xFX0FETUlOIiwidXNlcklkIjoiNSIsImlhdCI6MTY5MDkwNzUxOCwiZXhwIjoxNjkwOTA5MzE4fQ.C99Hgj_6tuC0WHpX1kMGY9IkOFPRVOwtOuA2P03iKfg", description = "valid time = 30 min")
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
