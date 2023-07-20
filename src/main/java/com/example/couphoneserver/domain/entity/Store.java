package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.Address;
import com.example.couphoneserver.domain.StoreStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "STORE")
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String name;
    @Embedded
    private Address address; // 불필요시 제거
    private Double longitude;
    private Double latitude;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand; // 해당 매장의 브랜드
}
