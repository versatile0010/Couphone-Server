package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
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

    @NoAuth
    @PostMapping("/login")
    @Operation(summary = "로그인(no Oauth2)", description =
                    """
                    Request Body 에 휴대폰 번호, 비밀번호를 담아서 보내주세요!
                    access token 과 refresh token 을 발급합니다.
                    access token 와 member Id 는 클라이언트에게 응답하고, refresh token 은 DB 에서 관리합니다.
                    반환받은 access token 은 api 요청 시 마다 Authentication 헤더 필드에 붙여서 request 해주세요.
                    서버에서는 Authentication 헤더 필드에 담긴 token 이 유효하면 api 를 동작합니다.
                    Auth 헤더 필드에 member Id 를 넣어서 요청하면 refresh token 이 유효하고 만료 기간 내이면 access token 을 재발급합니다.
                    """)
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("[AuthController.login]");
        return ResponseEntity.ok(memberService.signIn(loginRequestDto));
    }
}
