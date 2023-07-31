package com.example.couphoneserver.config;

import com.example.couphoneserver.utils.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    /**
     *  1. request header Authorization 내 Token 유효성 검사
     *  2. 유효하면 인증 정보를 security context 에 저장
     *  3. Token 기간 만료 시 userId 를 header 에서 가져옴
     *  4. userId 로 refresh token 을 조회한 뒤 유효성 검사이후 유효기간 전이라면 access 재발급
     */ 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request, HEADER_AUTHORIZATION);
        // access token 검증
        // TODO: 검증 로직 보완 예정
        if (StringUtils.hasText(accessToken) && !jwtProvider.isExpiredToken(accessToken)) {
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication); // security context 에 인증 정보 저장
            log.info("Access Token 은 아직 유효합니다.");
        } else if (StringUtils.hasText(accessToken) && jwtProvider.isExpiredToken(accessToken)) {
            // 재발급해야함
            log.info("Authorization 필드에 담겨진 Access Token 이 Expired 되었습니다!");
            String refreshToken = null;

            if (StringUtils.hasText(request.getHeader("Auth"))) { // Auth 에는 userId가 담겨서 와야함!
                Long userId = Long.parseLong(request.getHeader("Auth"));
                refreshToken = jwtProvider.getRefreshToken(userId); // userId로 refreshToken 조회
            }
            // refresh token 검증
            if (StringUtils.hasText(refreshToken) && !jwtProvider.isExpiredToken(refreshToken)) {
                // access token 재발급
                Authentication authentication = jwtProvider.getAuthentication(refreshToken);

                String newAccessToken = jwtProvider.generateAccessToken(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
                log.info("Access token 을 재발급합니다.");
            }
        }
        filterChain.doFilter(request, response);
    }

    public String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
