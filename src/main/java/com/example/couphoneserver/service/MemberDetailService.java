package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.MEMBER_NOT_FOUND;

/**
 * Spring Security 에서 사용자 정보를 가져옴
 */
@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
