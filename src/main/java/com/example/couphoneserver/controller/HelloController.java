package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트용 Hello 컨트롤러", description = "테스트용 컨트롤러입니다.")
@RestController
public class HelloController {
    @NoAuth
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
