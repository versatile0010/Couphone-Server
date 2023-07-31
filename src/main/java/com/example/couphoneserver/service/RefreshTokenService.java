package com.example.couphoneserver.service;

import com.example.couphoneserver.domain.entity.Member;
import com.example.couphoneserver.domain.entity.RefreshToken;
import com.example.couphoneserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    @Transactional
    public void saveOrUpdate(Member member, String refreshToken) {
        Optional<RefreshToken> findToken = refreshTokenRepository.findByMemberId(member.getId());
        RefreshToken token;
        if (findToken.isPresent()) {
            token = findToken.get();
            token.update(refreshToken);
        } else {
            token = new RefreshToken(member.getId(), refreshToken);
        }
        refreshTokenRepository.save(token);
    }
}
