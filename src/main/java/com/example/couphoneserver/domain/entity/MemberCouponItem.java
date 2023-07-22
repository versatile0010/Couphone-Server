package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.CouponItemStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_COUPON_ITEM")
@Entity
public class MemberCouponItem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_coupon_item_id")
    private Long id;

    private int stampCount;
    @Enumerated(EnumType.STRING)
    private CouponItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; // 해당 쿠폰을 가지고 있는 멤버

}
