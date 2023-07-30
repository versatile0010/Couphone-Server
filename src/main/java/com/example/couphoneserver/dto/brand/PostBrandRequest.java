package com.example.couphoneserver.dto.brand;

import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Category;
import com.example.couphoneserver.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBrandRequest {
    @Schema(example = "메가커피")
    @NotNull
    private String name;
    @Schema(example = "아이스 아메리카노 1잔 무료")
    @NotNull
    private String rewardDescription;
    @Schema(example = "----")
    private String brandImageUrl;

    @Schema(example = "1")
    private Long categoryId;

    public Brand toEntity(Category category) {
        return Brand.builder()
                .name(name)
                .rewardDescription(rewardDescription)
                .brandImageUrl(brandImageUrl)
                .category(category)
                .build();
    }
}
