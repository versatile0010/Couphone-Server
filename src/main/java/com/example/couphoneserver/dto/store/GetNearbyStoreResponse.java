package com.example.couphoneserver.dto.store;

import com.example.couphoneserver.dto.brand.GetBrandResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetNearbyStoreResponse {
    private Long store_id;
    private String name;
    private Long brand_id;
    private Double Longitude;
    private Double Latitude;
    private String Address;
    private double distance;
    private GetBrandResponse getBrandResponse;


    @Builder
    public GetNearbyStoreResponse(Long store_id, String name, Long brand_id, Double longitude, Double latitude, String address) {
        this.store_id = store_id;
        this.name = name;
        this.brand_id = brand_id;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.Address = address;
    }
}
