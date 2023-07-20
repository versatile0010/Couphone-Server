package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.common.argument_resolver.PreAuthorize;
import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.utils.jwt.JwtTokenProvider;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.couphoneserver.utils.BindingResultUtils.getErrorMessages;

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
