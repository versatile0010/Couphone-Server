package com.example.couphoneserver.config;

import com.example.couphoneserver.service.MemberDetailService;
import com.example.couphoneserver.service.RefreshTokenService;
import com.example.couphoneserver.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberDetailService memberDetailService;
    private final JwtTokenProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**", "/favicon.ico", "/swagger-ui");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)

                .authorizeHttpRequests((auth) -> auth.requestMatchers("/auth/login").permitAll())
                .authorizeHttpRequests((auth) -> auth.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new TokenAuthenticationFilter(jwtProvider, refreshTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), TokenAuthenticationFilter.class);
        return http.build();
    }

    // 인증 관리자 설정
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() throws Exception {
        return new CustomAuthenticationProvider(memberDetailService);
    }

    // 비밀번호 암호화 관련
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
