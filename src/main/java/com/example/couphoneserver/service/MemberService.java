package com.example.couphoneserver.service;

import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.AddMemberRequest;
import com.example.couphoneserver.dto.member.AddMemberResponse;
import com.example.couphoneserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 회원 관련 비지니스 로직
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     *  휴대폰 번호로 회원 가입
     */
    @Transactional
    public AddMemberResponse save(AddMemberRequest dto) throws UsernameNotFoundException {
        validateDuplicateMemberByPhoneNumber(dto.getPhoneNumber());
        validateDuplicateMemberByName(dto.getName());
        Member savedMember = memberRepository.save(Member.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(dto.getPassword())) // bCryptPasswordEncoder.encode
                .build()
        );
        return new AddMemberResponse(savedMember);
    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMemberByName(member.getName());
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 이름 중복 검증
     */
    private void validateDuplicateMemberByName(String name) {
        List<Member> findMembers = memberRepository.findByName(name);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("  이미 존재하는 회원입니다.  ");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단 건 조회 (by member_id)
     */
    public Member findOneById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("  주어진 id 와 매칭하는 회원을 찾을 수 없습니다.  "));
    }

    /**
     * 중복회원 검사
     */
    private void validateDuplicateMemberByPhoneNumber(String phoneNumber) {
        Optional<Member> optionalUser = memberRepository.findByPhoneNumber(phoneNumber);
        optionalUser.ifPresent(findUser -> {
            throw new IllegalArgumentException(" 휴대폰 번호가 중복입니다! ");
        });
    }
    /**
     *  휴대폰 번호로 회원 조회
     */
    public Optional<Member> getMemberByPhoneNumber(String phoneNumber){
        return memberRepository.findByPhoneNumber(phoneNumber);
    }
}
