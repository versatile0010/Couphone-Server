package com.example.couphoneserver.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class LoginRequestDto {
    @Email
    @NotNull
    @Schema(example = "aaa@naver.com", description = "이메일")
    private String email;
    @NotNull
    @Schema(example = "김이름", description = "사용자 이름")
    private String name;

}
