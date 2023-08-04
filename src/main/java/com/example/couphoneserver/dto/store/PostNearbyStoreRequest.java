package com.example.couphoneserver.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostNearbyStoreRequest {
    private static final int radius = 500;
    /*
     **버튼 있을 경우 ⇒ 지름 1km**

     **버튼 없을 경우 ⇒ 반지름 1km**
     */
    @NotNull(message = "longitude: {NotNull}")
    @Schema(example = "207005.189144674")
    private double longitude;
    @NotNull(message = "latitude: {NotNull}")
    @Schema(example = "449492.810069438")
    private double latitude;
    @NotNull(message = "is1km: {NotNull}")
    @Schema(example="true",description = "반지름이 1km인 경우 true를 넣어주세요")
    private Boolean is1km;
    @Nullable
    @Schema(description = "값을 넣어 보내지 마세요!")
    private double distance;

    public void setDistance() {
        this.distance = is1km?radius*2:radius;
    }
}
