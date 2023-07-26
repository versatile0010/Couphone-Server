package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
    Optional<Member> findByPhoneNumber(String phoneNumber); // phoneNumber 으로 사용자 정보를 가져옴.
}
