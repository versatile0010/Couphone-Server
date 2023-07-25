package com.example.couphoneserver.domain.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
}
