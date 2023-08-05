package com.example.couphoneserver.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
public class PostStoreResponse {
    @Schema(example = "1", description = "회원 아이디")
    private Long id;

    public PostStoreResponse(Long id) {
        this.id = id;
    }
}
