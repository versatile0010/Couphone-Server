package com.example.couphoneserver.dto.category;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetCategoryResponse {
    private Long CategoryId;
    private String CategoryName;
}
