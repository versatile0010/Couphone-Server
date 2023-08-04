package com.example.couphoneserver.repository.mappingInterface;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.dto.store.PostNearbyStoreResponse;

public interface StoreInfoMapping {
    Long getStore_id();
    String getName();
    String getAddress();
    Double getLongitude();
    Double getLatitude();
    Long getBrand_id();

    default Coordinate translateCoordinate(){
        return Coordinate.builder()
                .longitude(getLongitude())
                .latitude(getLatitude())
                .build();
    }

    default PostNearbyStoreResponse translateResponse(){
        return PostNearbyStoreResponse.builder()
                .store_id(getStore_id())
                .name(getName())
                .address(getAddress())
                .brand_id(getBrand_id())
                .build();
    }
}
