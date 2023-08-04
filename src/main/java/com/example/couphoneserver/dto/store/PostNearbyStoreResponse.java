package com.example.couphoneserver.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostNearbyStoreResponse {
    private Long store_id;
    private String name;
    private String address;
    private Long brand_id;
    private double distance;

    @Builder
    public PostNearbyStoreResponse(Long store_id, String name, String address, Long brand_id) {
        this.store_id = store_id;
        this.name = name;
        this.address = address;
        this.brand_id = brand_id;
    }
}
