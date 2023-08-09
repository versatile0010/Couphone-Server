package com.example.couphoneserver.dto.store;

import com.example.couphoneserver.dto.brand.GetBrandResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
    public GetNearbyStoreResponse(Long store_id, String name, Long brand_id, Double longitude, Double latitude, String address, GetBrandResponse getBrandResponse) {
        this.store_id = store_id;
        this.name = name;
        this.brand_id = brand_id;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.Address = address;
        this.getBrandResponse = getBrandResponse;
    }

    @Override
    public boolean equals(Object o) {
        GetNearbyStoreResponse that = (GetNearbyStoreResponse) o;
        return Objects.equals(store_id, that.store_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store_id);
    }
}
