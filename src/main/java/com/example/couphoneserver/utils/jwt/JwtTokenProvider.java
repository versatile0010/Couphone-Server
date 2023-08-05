package com.example.couphoneserver.utils.jwt;

import com.example.couphoneserver.common.exception.MemberException;
import com.example.couphoneserver.common.exception.jwt.bad_request.JwtBadRequestException;
import com.example.couphoneserver.common.exception.jwt.bad_request.JwtNoTokenException;
import com.example.couphoneserver.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import com.example.couphoneserver.common.exception.jwt.unauthorized.JwtExpiredTokenException;
import com.example.couphoneserver.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import com.example.couphoneserver.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import com.example.couphoneserver.domain.entity.RefreshToken;
import com.example.couphoneserver.repository.MemberRepository;
import com.example.couphoneserver.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.*;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean {

    @Value("${secret.jwt-secret-key}")
    private String JWT_SECRET_KEY;

    @Value("${secret.jwt-expired-in}")
    private long JWT_EXPIRED_IN;

    private Key key;
    // TODO: accessTokenValidTime, refreshTokenValidTime 환경변수 설정
    private final long accessTokenValidTime = (60 * 1000) * 30; // 30분
    private final long refreshTokenValidTime = (60 * 1000) * 60 * 24 * 7; // 7일
    private final String AUTHORITIES_KEY = "auth";
    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    @PostConstruct
    protected void init() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String generateToken(Authentication authentication, Long accessTokenValidTime) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenValidTime);

        String email = authentication.getName();
        Long userId = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND)).getId();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("userId", String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenValidTime);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshTokenValidTime);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()    // 1. token 으로부터 Claim 들을 가져온다.
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String authorities = claims.get("auth", String.class);
        // 2. 권한을 String 으로 가져온다.

        log.info("현재 회원의 권한은: " + authorities);

        List<String> rolesMap = Arrays.stream(authorities.split(",")).map(String::trim).toList();
        // 3. 전처리

        List<GrantedAuthority> authList = rolesMap.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        //4. List<GrantedAuthority> 형태로 만든다.

        User principal = new User(claims.getSubject(), "", authList);
        return new UsernamePasswordAuthenticationToken(principal, token, authList);
    }

    @Transactional(readOnly = true)
    public String getRefreshToken(Long userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(userId)
                .orElseThrow(() -> new JwtNoTokenException(JWT_ERROR));
        return refreshToken.getRefreshToken();
    }

    public boolean isExpiredToken(String token) throws JwtInvalidTokenException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.validateAccessToken]", e);
            throw e;
        }
    }

    public JwtCode validateToke(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            return JwtCode.EXPIRED;
        } catch (Exception e) {
            return JwtCode.DENIED;
        }
    }

    public String getPrincipal(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.get("userId", String.class));
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredTokenException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (IllegalArgumentException e) {
            throw new JwtBadRequestException(JWT_ERROR);
        }
    }

    public Long getUserIdFromExpiredToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.get("userId", String.class));
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에서 사용자 ID를 추출
            // access token 이 만료되었지만 refresh token 이 존재하는 경우
            // JwtExceptionFilter 에 의해 ExpiredException 이 처리되기 전에 userId 를 반환해야 할 때에만 사용
            Claims expiredClaims = e.getClaims();
            return Long.parseLong(expiredClaims.get("userId", String.class));
        }
    }

}
