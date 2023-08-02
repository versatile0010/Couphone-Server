package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.response.MemberInfoResponseDto;
import com.example.couphoneserver.dto.member.response.MemberResponseDto;
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
    public BaseResponse<MemberResponseDto> delete(@PathVariable("member-id") Long memberId) {
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.delete(member));
    }

    @GetMapping("/{member-id}")
    @Operation(summary = "회원 정보 조회", description =
            "회원 정보를 조회합니다. path variable 로 멤버 id 담아서 보내주세요!")
    public BaseResponse<MemberInfoResponseDto> show(@PathVariable("member-id") Long memberId) {
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.getMemberInfo(member));
    }
}
