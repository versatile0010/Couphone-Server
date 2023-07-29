package com.example.couphoneserver.controller;

import com.example.couphoneserver.dto.member.request.LoginRequestDto;
import com.example.couphoneserver.dto.member.response.LoginResponseDto;
import com.example.couphoneserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    //@NoAuth
    @PostMapping("/login")
    @Operation(summary = "로그인(no Oauth2)", description = "로그인하여 access token, refresh token 을 발급합니다.")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("[AuthController.login]");
        return ResponseEntity.ok(memberService.signIn(loginRequestDto));
    }
}
