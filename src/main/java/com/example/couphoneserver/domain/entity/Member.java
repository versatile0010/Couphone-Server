package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *  Member (회원 정보) 엔티티
 *  MemberCouponItem(회원 쿠폰) 과 1:N 연관관계
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToMany(mappedBy = "member")
    private List<MemberCouponItem> coupons = new ArrayList<>();

    @Builder
    public Member(String name, String email, MemberGrade grade){
        this.name = name;
        this.email = email;
        this.grade = grade;
    }
}
