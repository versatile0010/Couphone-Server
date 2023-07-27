package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.dto.member.AddMemberRequest;
import com.example.couphoneserver.dto.member.MemberInfoResponseDto;
import com.example.couphoneserver.dto.member.MemberResponseDto;
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
    @Operation(summary = "휴대폰 번호로 회원 가입", description = "request = {이름, 휴대폰 번호, 비밀번호}")
    public BaseResponse<MemberResponseDto> signup(@Valid @RequestBody AddMemberRequest request) {
        return new BaseResponse<>(memberService.save(request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "회원 탈퇴", description = "회원의 상태를 TERMINATED 로 변경합니다.")
    public BaseResponse<MemberResponseDto> delete(@PathVariable Long id){
        Member member = memberService.findOneById(id);
        return new BaseResponse<>(memberService.delete(member));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 정보 조회", description = "회원의 정보를 조회합니다.")
    public BaseResponse<MemberInfoResponseDto> show(@PathVariable Long id){
        Member member = memberService.findOneById(id);
        return new BaseResponse<>(memberService.getMemberInfo(member));
    }
}
