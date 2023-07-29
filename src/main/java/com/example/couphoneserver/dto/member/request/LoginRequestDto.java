package com.example.couphoneserver.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class LoginRequestDto {
    @Schema(example = "010-1234-1234", description = "전화번호")
    private String phoneNumber;
    @Schema(example = "!32124asd@", description = "암호화된 비밀번호")
    private String password;

}
