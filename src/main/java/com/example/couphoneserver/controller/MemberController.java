package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.response.GetMemberCouponBrandsResponse;
import com.example.couphoneserver.dto.member.response.GetMemberResponse;
import com.example.couphoneserver.dto.member.response.PatchMemberResponse;
import com.example.couphoneserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 컨트롤러", description = "회원 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {
    private final MemberService memberService;

    @PatchMapping("/{member-id}")
    @Operation(summary = "회원 탈퇴", description =
            "회원의 상태를 TERMINATED 으로 변경합니다. path variable 로 멤버 id 담아서 보내주세요!")
    public BaseResponse<PatchMemberResponse> delete(@PathVariable("member-id") Long memberId) {
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.delete(member));
    }

    @GetMapping("/{member-id}")
    @Operation(summary = "회원 정보 조회", description =
            "회원 정보를 조회합니다. path variable 로 멤버 id 담아서 보내주세요!")
    public BaseResponse<GetMemberResponse> show(@PathVariable("member-id") Long memberId) {
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.getMemberInfo(member));
    }

    @GetMapping("/{member-id}/brands")
    @Operation(summary = "정렬 조건에 따른 브랜드 조회", description =
            """
                    path variable 으로 member-id 를 보내면 해당 회원이 가지고 있는 쿠폰 브랜드 리스트를 반환합니다.
                    정렬 조건은 query string 으로 sort 값을 보내주세요. {1, 2, 3} 에 따라 달라집니다.
                    - 1(default)번 옵션은 쿠폰 많은 순, 생성 시간이 이른 순
                    - 2번 옵션은 생성 시간이 이른 순, 쿠폰 많은 순
                    - 3번 옵션은 브랜드 이름 순으로 정렬하여 데이터를 반환합니다.
                    """)
    public BaseResponse<GetMemberCouponBrandsResponse> getBrands(
            @PathVariable("member-id") Long memberId,
            @RequestParam(required = false, defaultValue = "1", value = "sort") String sort) {
        return new BaseResponse<>(memberService.getBrands(memberId, Integer.parseInt(sort)));
    }
}
