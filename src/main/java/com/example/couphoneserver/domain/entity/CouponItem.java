package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.CouponItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "COUPON_ITEM")
@Entity
@Builder
@AllArgsConstructor
public class CouponItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_item_id")
    private Long id;
    private int stampCount;
    @Enumerated(EnumType.STRING)
    private CouponItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 해당 쿠폰을 가지고 있는 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand; // 해당 쿠폰의 사용처(브랜드)

    public void SetBrand(Brand brand) {
        this.brand = brand;
    }

    public void setStatus(CouponItemStatus status) {
        this.status = status;
    }

    /**
     * 스탬프 적립
     */
    public void collectStamp() {
        int requiredStampCount = 10; // 필요한 쿠폰 개수
        int currentStampCount = this.stampCount;
        this.stampCount = currentStampCount + 1;

        if (currentStampCount + 1 == requiredStampCount) {
            this.status = CouponItemStatus.ACTIVE;
        }

    }

    /**
     * 스탬프 회수
     */
    public void retrieveStamp() {
        int currentStampCount = this.stampCount;
        if (currentStampCount >= 1) {
            this.stampCount = currentStampCount - 1;
        }
    }


    @Builder
    public CouponItem(Member member, Brand brand) {
        this.member = member;
        this.brand = brand;
        this.status = CouponItemStatus.INACTIVE;
    }

    public boolean isExpired() {
        LocalDateTime createdDateTime = this.getCreatedDate();
        LocalDateTime expiryDateTime = createdDateTime.plusMonths(6);
        // 만료일 이후이면 true (해당 쿠폰은 만료됨 )
        return LocalDateTime.now().isAfter(expiryDateTime);
    }
}
