package com.treetoplodge.treetoplodge_api.Service;

import com.treetoplodge.treetoplodge_api.model.RefreshToken;
import com.treetoplodge.treetoplodge_api.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    
    RefreshToken createRefreshToken(String userId);
    
    RefreshToken verifyExpiration(RefreshToken token);
    
    int deleteByUserId(String userId);
    
    void deleteByUser(Long id);
}
