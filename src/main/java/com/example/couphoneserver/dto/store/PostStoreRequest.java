package com.example.couphoneserver.dto.store;

import com.example.couphoneserver.domain.StoreStatus;
import com.example.couphoneserver.domain.entity.Brand;
import com.example.couphoneserver.domain.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostStoreRequest {
    @NotNull(message = "bid: {NotNull}")
    @Range(min=1,message="bid: 브랜드 아이디는 {min} 이상부터 가능합니다")
    @Schema(example="1", description = "브랜드 등록 후 응답값으로 받은 브랜드 아이디를 넣어주세요.")
    private Long bid;

    @NotBlank(message = "name: {NotBlank}")
    @Size(min=1,max=100,message = "name: 가게 이름의 길이는 {min} 이상, {max} 이하 가능합니다")
    @Schema(example="건국대학교 서울캠퍼스",description = "브랜드 이름+공백+지점명으로 보내주세요.")
    private String name;
    @NotBlank(message = "address: {NotBlank}")
    @Schema(example = "서울특별시 광진구 능동로 120")
    private String address;
    @NotNull(message = "longitude: {NotNull}")
    @Schema(example = "123456.111111")
    private Double longitude;
    @NotNull(message = "latitude: {NotNull}")
    @Schema(example = "123456.111111")
    private Double latitude;

    public Store toEntity(Brand brand) {
        return Store.builder()
                .name(name)
                .address(address)
                .longitude(longitude)
                .latitude(latitude)
                .status(StoreStatus.ACTIVE)
                .brand(brand)
                .build();
    }
}
