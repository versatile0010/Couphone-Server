package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.member.request.LoginRequestDto;
import com.example.couphoneserver.dto.member.response.LoginResponseDto;
import com.example.couphoneserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Operation(summary = "로그인(Oauth2)", description =
            """
                    request body 에 이메일, 이름을 포함시켜서 보내주세요.
                    access token 과 refresh token 을 반환합니다.
                    - 신규 회원이면 회원 가입 처리 후 로그인 처리합니다.
                    - 기존 회원이면 바로 로그인 처리 합니다.
                    """)
    public BaseResponse<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("[AuthController.login]");
        if (!memberService.isExistingMember(loginRequestDto)) {
            // 신규 회원일 경우에는 회원 가입 처리
            log.info("[신규 회원이므로 회원 가입 처리 합니다.]");
            memberService.saveByEmailAndName(loginRequestDto);
        }
        // 가입 처리 된 회원인 경우 login 진행
        log.info("[로그인을 진행합니다.]");
        return new BaseResponse<>(memberService.signIn(loginRequestDto));
    }
}
