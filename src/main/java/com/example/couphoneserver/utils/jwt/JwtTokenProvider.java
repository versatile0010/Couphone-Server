package com.example.couphoneserver.utils.jwt;

import com.example.couphoneserver.common.exception.jwt.bad_request.JwtNoTokenException;
import com.example.couphoneserver.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import com.example.couphoneserver.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import com.example.couphoneserver.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import com.example.couphoneserver.domain.entity.RefreshToken;
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

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_MEMBER"), new SimpleGrantedAuthority("ROLE_ADMIN"));

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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
        return claims.get("userId", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
