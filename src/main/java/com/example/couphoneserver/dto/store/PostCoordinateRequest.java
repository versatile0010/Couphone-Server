package com.example.couphoneserver.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCoordinateRequest {
    @NotBlank(message = "address: {NotBlank}")
    @Schema(example = "서울특별시 광진구 능동로 120")
    private String address;
}
