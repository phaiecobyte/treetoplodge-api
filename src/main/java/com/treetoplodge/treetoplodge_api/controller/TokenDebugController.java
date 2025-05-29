package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.Service.impl.RefreshTokenServiceImpl;
import com.treetoplodge.treetoplodge_api.model.RefreshToken;
import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.repository.RefreshTokenRepository;
import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public/debug/tokens")
@RequiredArgsConstructor
@Slf4j
public class TokenDebugController {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenServiceImpl refreshTokenService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTokens() {
        List<Map<String, Object>> tokens = refreshTokenRepository.findAll().stream()
                .map(token -> {
                    Map<String, Object> tokenMap = new HashMap<>();
                    tokenMap.put("id", token.getId());
                    tokenMap.put("token", token.getToken().substring(0, 10) + "...");
                    tokenMap.put("userId", token.getUser().getId());
                    tokenMap.put("username", token.getUser().getUsername());
                    tokenMap.put("expiryDate", token.getExpiryDate());
                    return tokenMap;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(tokens);
    }
    
    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteTokens(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int deleted = refreshTokenRepository.deleteByUserId(userId);
        
        response.put("deleted", deleted);
        response.put("userId", userId);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/user/{username}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteTokensByUsername(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
        
        int deleted = refreshTokenRepository.deleteByUserId(user.getId());
        
        response.put("deleted", deleted);
        response.put("userId", user.getId());
        response.put("username", username);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/cleanup")
    @Transactional
    public ResponseEntity<Map<String, Object>> cleanupAllTokens() {
        Map<String, Object> response = new HashMap<>();
        
        long count = refreshTokenRepository.count();
        refreshTokenRepository.deleteAll();
        
        response.put("deletedCount", count);
        response.put("message", "All refresh tokens have been deleted");
        
        return ResponseEntity.ok(response);
    }
}