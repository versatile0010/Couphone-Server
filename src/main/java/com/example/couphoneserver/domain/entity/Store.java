package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.StoreStatus;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "STORE")
@ToString
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String name;
    private String address;
    private Double longitude;
    private Double latitude;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand; // 해당 매장의 브랜드

    @Builder
    public Store(String name, String address, Double longitude, Double latitude, StoreStatus status, Brand brand) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.brand = brand;
    }

}
