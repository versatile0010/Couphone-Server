package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    @NoAuth
    @PostMapping("/login")
    public BaseResponse<String> login(String userId) {
        log.info("[AuthController.login]");

        log.info(userId);

        String JwtToken = jwtTokenProvider.createToken("data", Long.parseLong(userId));

        return new BaseResponse<>(JwtToken);
    }
}
