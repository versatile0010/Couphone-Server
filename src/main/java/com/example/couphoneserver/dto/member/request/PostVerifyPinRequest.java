package com.example.couphoneserver.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVerifyPinRequest {
    @NotNull
    @Schema(description = "핀 번호(숫자 6자리)", example = "012345")
    private String pinNumber;
}
