package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.request.LoginRequestDto;
import com.example.couphoneserver.dto.member.response.LoginResponseDto;
import com.example.couphoneserver.dto.member.response.MemberInfoResponseDto;
import com.example.couphoneserver.dto.member.response.MemberResponseDto;
import com.example.couphoneserver.repository.MemberRepository;
import com.example.couphoneserver.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.DUPLICATED_MEMBER_EXCEPTION;
import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.MEMBER_NOT_FOUND;

/**
 * 회원 관련 비지니스 로직
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void saveByEmailAndName(LoginRequestDto dto) throws UsernameNotFoundException {
        validateDuplicateMemberByEmail(dto.getEmail());
        validateDuplicateMemberByName(dto.getName());
        memberRepository.save(
                new Member(dto.getName(), dto.getEmail(),
                        MemberStatus.ACTIVE, MemberGrade.ROLE_MEMBER)
        );
    }

    public boolean isExistingMember(LoginRequestDto requestDto) throws UsernameNotFoundException {
        String email = requestDto.getEmail();
        return memberRepository.findMemberByEmail(email).isPresent();
    }

    @Transactional
    public void setActive(Member member) {
        member.setActive();
    }

    @Transactional
    public void setGrade(Member member, MemberGrade grade) {
        member.setGrade(grade);
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
     * 회원 탈퇴 처리
     */
    @Transactional
    public MemberResponseDto delete(Member member) {
        member.setTerminated();
        return new MemberResponseDto(member);
    }

    @Transactional
    public LoginResponseDto signIn(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getName());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        Member member = (Member) authentication.getPrincipal();

        refreshTokenService.saveOrUpdate(member, refreshToken);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("JWT Bearer ")
                .memberId(member.getId())
                .grade(member.getGrade())
                .build();
    }

    public Member findOneByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    /**
     * 단일 회원 정보 조회
     */
    public MemberInfoResponseDto getMemberInfo(Member member) {
        return new MemberInfoResponseDto(member);
    }

    /**
     * 이름 중복 검증
     */
    private void validateDuplicateMemberByName(String name) {
        List<Member> findMembers = memberRepository.findMembersByName(name);
        if (!findMembers.isEmpty()) {
            throw new MemberException(DUPLICATED_MEMBER_EXCEPTION);
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
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    /**
     * 중복회원 검사
     */
    private void validateDuplicateMemberByEmail(String email) {
        Optional<Member> optionalUser = memberRepository.findMemberByEmail(email);
        optionalUser.ifPresent(findUser -> {
            throw new MemberException(DUPLICATED_MEMBER_EXCEPTION);
        });
    }

    /**
     * 휴대폰 번호로 회원 조회
     */
    public Optional<Member> getMemberByPhoneNumber(String phoneNumber) {
        return memberRepository.findMemberByPhoneNumber(phoneNumber);
    }

    /**
     * 이메일로 회원 조회
     */
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }


}