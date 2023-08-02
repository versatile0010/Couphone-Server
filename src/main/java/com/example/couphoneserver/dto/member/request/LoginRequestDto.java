package com.example.couphoneserver.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class LoginRequestDto {
    @Email
    @Schema(example = "aaa@naver.com", description = "이메일")
    private String email;
    @Schema(example = "!32124asd@", description = "비밀번호")
    private String password;

}
