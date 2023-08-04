package com.example.couphoneserver.config;

import com.example.couphoneserver.common.exception.jwt.bad_request.JwtNoTokenException;
import com.example.couphoneserver.common.exception.jwt.unauthorized.JwtExpiredTokenException;
import com.example.couphoneserver.service.RefreshTokenService;
import com.example.couphoneserver.utils.jwt.JwtCode;
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

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.EXPIRED_TOKEN;
import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.TOKEN_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final RefreshTokenService tokenService;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (permitAllUrl.of(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = resolveToken(request, HEADER_AUTHORIZATION); // 1. 사용자가 보낸 토큰을 확인

        if (StringUtils.hasText(accessToken) && jwtProvider.validateToke(accessToken) == JwtCode.ACCESS) {
            // Access token 이 유효하면
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication); // security context 에 인증 정보 저장
            log.info("Access Token 은 아직 유효합니다.");
        } else if (StringUtils.hasText(accessToken) && jwtProvider.validateToke(accessToken) == JwtCode.EXPIRED) {
            // 2-1. Access token 이 만료되었다면
            log.info("Authorization 필드에 담겨진 Access Token 이 Expired 되었습니다!");
            String refreshToken = null;

            // 2-2. jwt token 으로부터 userId 를 찾고, 해당 userId 에 대한 refreshToken 을 탐색
            Long userId = jwtProvider.getUserIdFromExpiredToken(accessToken);
            log.warn(userId.toString());
            refreshToken = jwtProvider.getRefreshToken(userId);

            // refresh token 이 존재하고 유효하다면
            if (StringUtils.hasText(refreshToken) && jwtProvider.validateToke(refreshToken) == JwtCode.ACCESS) {
                // access token 재발급
                log.info("해당 회원 ID 에 대한 Refresh token 이 존재하고 유효합니다.");
                log.info("Access token 을 재발급하여 반환합니다.");

                Authentication authentication = jwtProvider.getAuthentication(refreshToken);

                String newAccessToken = jwtProvider.generateAccessToken(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
            } else if (StringUtils.hasText(refreshToken) && jwtProvider.validateToke(refreshToken) == JwtCode.EXPIRED) {
                // refresh token 이 존재하지만 만료되었다면
                log.warn("해당 회원 ID 에 대한 Refresh token 이 존재하지만, 만료되었습니다.");
                throw new JwtExpiredTokenException(EXPIRED_TOKEN);
            }
            if (refreshToken == null) {
                // refresh token 이 존재하지 않으면
                log.warn("해당 회원 ID 에 대한 Refresh token 이 존재하지 않습니다.");
                throw new JwtNoTokenException(TOKEN_NOT_FOUND);
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
