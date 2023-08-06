package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.request.PatchMemberPhoneNumberRequest;
import com.example.couphoneserver.dto.member.response.GetMemberCouponBrandsResponse;
import com.example.couphoneserver.dto.member.response.GetMemberResponse;
import com.example.couphoneserver.dto.member.response.PatchMemberResponse;
import com.example.couphoneserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @PatchMapping("")
    @Operation(summary = "회원 탈퇴", description =
            """
                    회원의 상태를 TERMINATED 으로 변경합니다.
                    - [ROLE_MEMBER OR ROLE_ADMIN]
                    - access token 을 반드시 포함해서 보내주세요!
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<PatchMemberResponse> delete(Principal principal) {
        Long memberId = memberService.findMemberIdByPrincipal(principal);
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.delete(member));
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("")
    @Operation(summary = "회원 정보 조회", description =
            """
                    회원 정보를 조회합니다.
                    - [ROLE_MEMBER OR ROLE_ADMIN]
                    - access token 을 반드시 포함해서 보내주세요!
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<GetMemberResponse> show(Principal principal) {
        Long memberId = memberService.findMemberIdByPrincipal(principal);
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.getMemberInfo(member));
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @GetMapping("/brands")
    @Operation(summary = "정렬 조건에 따른 브랜드 조회", description =
            """
                    해당 회원이 가지고 있는 쿠폰 브랜드 리스트를 반환합니다.
                    정렬 조건은 query string 으로 sort 값을 보내주세요. {1, 2, 3} 에 따라 달라집니다.
                    - [ROLE_MEMBER OR ROLE_ADMIN]
                    - Access token 을 반드시 포함해서 보내주세요!
                    - 1(default)번 옵션은 쿠폰 많은 순, 생성 시간이 이른 순
                    - 2번 옵션은 생성 시간이 이른 순, 쿠폰 많은 순
                    - 3번 옵션은 브랜드 이름 순으로 정렬하여 데이터를 반환합니다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<GetMemberCouponBrandsResponse> getBrands(
            Principal principal,
            @RequestParam(required = false, defaultValue = "1", value = "sort") String sort) {
        Long memberId = memberService.findMemberIdByPrincipal(principal);
        return new BaseResponse<>(memberService.getBrands(memberId, Integer.parseInt(sort)));
    }

    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    @PatchMapping("/phone-number")
    @Operation(summary = "회원의 전화번호 변경", description =
            """
                    회원의 전화번호를 변경/설정합니다.
                    - [ROLE_MEMBER OR ROLE_ADMIN]
                    - Access token 을 반드시 포함해서 보내주세요!
                    - request body 에 phoneNumber 을 보내주세요.
                    - 전화번호는 반드시 010-1234-1234 형태로 넘겨져야 합니다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    public BaseResponse<PatchMemberResponse> setPhoneNumber(Principal principal,
                                                            @Valid @RequestBody PatchMemberPhoneNumberRequest request) {
        Long memberId = memberService.findMemberIdByPrincipal(principal);
        return new BaseResponse<>(memberService.setMemberPhoneNumber(request, memberId));
    }
}
