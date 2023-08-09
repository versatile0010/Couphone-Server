package com.example.couphoneserver.repository.mappingInterface;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.dto.store.GetNearbyStoreResponse;

public interface StoreInfoMapping {
    Long getStore_id();
    String getName();
    Double getLongitude();
    Double getLatitude();
    Long getBrand_id();
    String getAddress();

    default Coordinate translateCoordinate(){
        return Coordinate.builder()
                .longitude(getLongitude())
                .latitude(getLatitude())
                .build();
    }

    default GetNearbyStoreResponse translateResponse(){
        return GetNearbyStoreResponse.builder()
                .store_id(getStore_id())
                .name(getName())
                .brand_id(getBrand_id())
                .latitude(getLatitude())
                .longitude(getLongitude())
                .address(getAddress())
                .build();
    }
}
