package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Member (회원 정보) 엔티티
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToMany(mappedBy = "member")
    private List<CouponItem> coupons = new ArrayList<>();

    @Builder
    public Member(String name, String email, String password, String auth) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(String name, String email, String password, MemberStatus status, MemberGrade grade) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.grade = grade;
    }

    public Member(String name, String email, MemberStatus status, MemberGrade grade) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.grade = grade;
    }

    // [Spring Security] 사용자 인증 정보 접근

    /**
     * 사용자가 가지고 있는 권한의 목록을 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (grade.getValue().equals("ROLE_MEMBER")) {
            return List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * 사용자의 비밀번호를 반환
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 사용자를 식별할 수 있는 사용자 이름을 반환함
     */
    @Override
    public String getUsername() {
        return email; // 사용자의 고유한 값을 반환
    }

    /**
     * 만료된 계정이면 false 를 반환
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // 유효
    }

    /**
     * 잠겨진 계정이면 false 를 반환
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // unlock
    }

    /**
     * 만료되지 않은 비밀번호이면 true 를 반환
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 유효
    }

    /**
     * 사용가능한 계정이면 true 를 반환
     */
    @Override
    public boolean isEnabled() {
        return true; // 사용가능
    }

    public void setTerminated() {
        status = MemberStatus.TERMINATED;
    }

    public void setActive() {
        this.status = MemberStatus.ACTIVE;
    }

    public void setGrade(MemberGrade grade) {
        this.grade = grade;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
