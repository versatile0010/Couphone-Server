package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.member.AddMemberRequest;
import com.example.couphoneserver.dto.member.AddMemberResponse;
import com.example.couphoneserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse<AddMemberResponse> signup(@Valid @RequestBody AddMemberRequest request) {
        return new BaseResponse<>(memberService.save(request));
    }

}
