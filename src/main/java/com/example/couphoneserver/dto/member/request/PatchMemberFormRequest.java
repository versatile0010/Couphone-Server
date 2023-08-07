package com.example.couphoneserver.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMemberFormRequest {
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화 번호는 반드시 010-1111-1111 형식이어야 합니다.")
    @Schema(description = "휴대폰 번호", example = "010-1111-1111")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{6}$", message = "핀 번호는 반드시 숫자 6자리여야 합니다.")
    @Schema(description = "핀 번호(숫자 6자리)", example = "012345")
    private String pinNumber;
}
