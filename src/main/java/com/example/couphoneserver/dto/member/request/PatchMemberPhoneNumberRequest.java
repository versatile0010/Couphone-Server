package com.example.couphoneserver.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMemberPhoneNumberRequest {
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    @Schema(description = "휴대폰 번호", example = "010-1111-1111")
    private String phoneNumber;
}
