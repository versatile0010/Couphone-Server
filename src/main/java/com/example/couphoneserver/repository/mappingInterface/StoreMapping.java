package com.example.couphoneserver.repository.mappingInterface;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.store.GetNearbyStoreResponse;

import java.time.LocalDateTime;

public interface StoreMapping {
    Long getStore_id();
    String getName();
    Double getLongitude();
    Double getLatitude();
    String getAddress();
    Long getBrand_id();
    String getBrandName();
    String getReward_description();
    String getBrand_image_url();
    LocalDateTime getCreated_date();
    Integer getStamp_Count();

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
                .getBrandResponse(translateGetBrandResponse())
                .build();
    }

    default GetBrandResponse translateGetBrandResponse(){
        return GetBrandResponse.builder()
                .id(getBrand_id())
                .name(getBrandName())
                .brandImageUrl(getBrand_image_url())
                .rewardDescription(getReward_description())
                .createdDate(getCreated_date())
                .stampCount(0)
                .build();
    }
}
