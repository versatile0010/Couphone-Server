package com.example.couphoneserver.service;

import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.domain.MemberGrade;
import com.example.couphoneserver.domain.MemberStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.auth.LoginRequestDto;
import com.example.couphoneserver.dto.auth.LoginResponseDto;
import com.example.couphoneserver.dto.brand.GetBrandResponse;
import com.example.couphoneserver.dto.member.request.PatchMemberFormRequest;
import com.example.couphoneserver.dto.member.response.*;
import com.example.couphoneserver.repository.BrandRepository;
import com.example.couphoneserver.repository.CouponItemRepository;
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

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.*;

/**
 * 회원 관련 비지니스 로직
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CouponItemRepository couponItemRepository;

    private final BrandRepository brandRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void saveByEmailAndName(LoginRequestDto dto) throws UsernameNotFoundException {
        validateDuplicateMemberByEmail(dto.getEmail());
        validateDuplicateMemberByName(dto.getName());

        if (dto.getRole().equals("admin")) {
            memberRepository.save(
                    new Member(dto.getName(), dto.getEmail(),
                            MemberStatus.ACTIVE, MemberGrade.ROLE_ADMIN)
            );
        } else {
            memberRepository.save(
                    new Member(dto.getName(), dto.getEmail(),
                            MemberStatus.ACTIVE, MemberGrade.ROLE_MEMBER)
            );
        }
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
    public PatchMemberResponse delete(Member member) {
        member.setTerminated();
        return new PatchMemberResponse(member);
    }

    @Transactional
    public LoginResponseDto signIn(LoginRequestDto loginRequestDto, String memberLabel) {
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
                .memberLabel(memberLabel)
                .build();
    }

    public Member findOneByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    /**
     * 단일 회원 정보 조회
     */
    public GetMemberResponse getMemberInfo(Member member) {
        return new GetMemberResponse(member);
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


    public GetMemberCouponBrandsResponse getBrands(Long memberId, int option) {
        // 회원이 가지고 있는 쿠폰의 브랜드 정보 리스트와 회원 정보를 같이 반환합니다.
        // 이때 정렬 기준은 option 에 따라서 달라집니다. (default 는 쿠폰 많은 순, 생성시간 이른 순)
        List<CouponItem> coupons;
        switch (option) {
            case 2 -> {
                log.info("[정렬 필터 옵션2번]");
                coupons = couponItemRepository.findByMemberIdOrderByCreatedDateAndStampCount(memberId);
            }
            case 3 -> {
                log.info("[정렬 필터 옵션3번]");
                coupons = couponItemRepository.findByMemberIdOrderByBrandName(memberId);
            }
            default -> {
                log.info("[정렬 필터 옵션1번]");
                coupons = couponItemRepository.findByMemberIdOrderByStampCountAndCreatedDate(memberId);
            }
        }
        List<BrandDto> brands = coupons.stream().map(coupon -> {
            GetBrandResponse brandInfo = new GetBrandResponse(coupon.getBrand(), coupon.getStampCount(), coupon.getCreatedDate());
            return new BrandDto(brandInfo, coupon.getStatus());
        }).toList();

        Member member = findOneById(memberId);
        return new GetMemberCouponBrandsResponse(member, brands);
    }

    public Long findMemberIdByPrincipal(Principal principal) {
        String email = principal.getName();
        return findOneByEmail(email).getId();
    }

    public Member findMemberByPrincipal(Principal principal) {
        String email = principal.getName();
        return findOneByEmail(email);
    }

    @Transactional
    public PatchMemberResponse setMemberPhoneNumberAndPinNumber(PatchMemberFormRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        String phoneNumber = request.getPhoneNumber();
        String encodedNumber = bCryptPasswordEncoder.encode(request.getPinNumber());
        member.setPhoneNumber(phoneNumber);
        member.setPassword(encodedNumber);
        return new PatchMemberResponse(member);
    }

    public PostVerifyPinResponse verifyPassword(Member member, String rawPassword) {
        if (bCryptPasswordEncoder.matches(rawPassword, member.getPassword())) {
            return new PostVerifyPinResponse(member);
        } else {
            throw new MemberException(MEMBER_PIN_NOT_MATCHED);
        }
    }
}