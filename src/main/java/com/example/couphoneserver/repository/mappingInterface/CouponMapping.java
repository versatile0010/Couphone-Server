package com.example.couphoneserver.repository.mappingInterface;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.member.response.BrandDto;

import java.time.LocalDateTime;

public interface CouponMapping {
    String getName();
    Long getBrand_id();
    String getReward_description();
    String getBrand_image_url();
    String getStatus();
    Integer getStamp_Count();
    LocalDateTime getCreated_date();

    default BrandDto translateBrandDto(){
        return BrandDto.builder()
                .brand(translateGetBrandResponse())
                .status(translateCouponItemStatus())
                .build();
    }

    default CouponItemStatus translateCouponItemStatus(){
        return CouponItemStatus.valueOf(getStatus());
    }


    default GetBrandResponse translateGetBrandResponse(){
        return GetBrandResponse.builder()
                .id(getBrand_id())
                .name(getName())
                .brandImageUrl(getBrand_image_url())
                .rewardDescription(getReward_description())
                .createdDate(getCreated_date())
                .stampCount(getStamp_Count())
                .build();
    }
}
