package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.request.AddMemberRequestDto;
import com.example.couphoneserver.dto.member.response.MemberInfoResponseDto;
import com.example.couphoneserver.dto.member.response.MemberResponseDto;
import com.example.couphoneserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @NoAuth
    @PostMapping
    @Operation(summary = "휴대폰 번호로 회원 가입", description =
            "Request Body 에 휴대폰 번호, 비밀번호를 담아서 보내주세요! 비밀번호는 DB 에 암호화되어 관리합니다.")
    public BaseResponse<MemberResponseDto> signup(@Valid @RequestBody AddMemberRequestDto request) {
        return new BaseResponse<>(memberService.save(request));
    }

    @PatchMapping("/{member-id}")
    @Operation(summary = "회원 탈퇴", description =
            "회원의 상태를 TERMINATED 으로 변경합니다. path variable 로 멤버 id 담아서 보내주세요!")
    public BaseResponse<MemberResponseDto> delete(@PathVariable("member-id") Long memberId){
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.delete(member));
    }

    @GetMapping("/{member-id}")
    @Operation(summary = "회원 정보 조회", description =
            "회원 정보를 조회합니다. path variable 로 멤버 id 담아서 보내주세요!")
    public BaseResponse<MemberInfoResponseDto> show(@PathVariable("member-id") Long memberId){
        Member member = memberService.findOneById(memberId);
        return new BaseResponse<>(memberService.getMemberInfo(member));
    }
}
