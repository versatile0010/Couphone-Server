package com.example.couphoneserver.utils.jwt;

public enum JwtCode {
    ACCESS("토큰이 유효합니다."),
    EXPIRED("만료된 토큰입니다."),
    DENIED("유효하지 않은 토큰입니다.");

    private final String description;

    JwtCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
