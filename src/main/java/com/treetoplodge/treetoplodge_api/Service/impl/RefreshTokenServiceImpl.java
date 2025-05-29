package com.treetoplodge.treetoplodge_api.Service.impl;

import com.treetoplodge.treetoplodge_api.Service.RefreshTokenService;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.RefreshToken;
import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.repository.RefreshTokenRepository;
import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.jwt.refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String userId) {
        log.info("Creating refresh token for user ID: {}", userId);
        
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return AppException.notFound("User not found with ID: " + userId);
                });
        
        log.info("Found user {} with ID {}", user.getUsername(), user.getId());
        
        // First attempt a direct delete by user ID - this should be more reliable
        int deleted = refreshTokenRepository.deleteByUserId(user.getId());
        log.info("Deleted {} existing refresh tokens for user ID {}", deleted, user.getId());
        
        // Create new token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        
        // Save and return
        try {
            refreshToken = refreshTokenRepository.save(refreshToken);
            log.info("Successfully created refresh token for user {}", user.getUsername());
            return refreshToken;
        } catch (Exception e) {
            log.error("Error saving refresh token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create refresh token", e);
        }
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw AppException.badRequest(
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Override
    @Transactional
    public int deleteByUserId(String userId) {
        log.info("Deleting refresh tokens for user ID: {}", userId);
        
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return AppException.notFound("User not found with ID: " + userId);
                });
        
        int deleted = refreshTokenRepository.deleteByUserId(user.getId());
        log.info("Deleted {} refresh tokens for user {}", deleted, user.getUsername());
        return deleted;
    }
    
    @Override
    @Transactional
    public void deleteByUser(Long id) {
        log.info("Deleting refresh tokens for user with database ID: {}", id);
        int deleted = refreshTokenRepository.deleteByUserId(id);
        log.info("Deleted {} refresh tokens for user with database ID {}", deleted, id);
    }
}
