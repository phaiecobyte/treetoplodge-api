package com.treetoplodge.treetoplodge_api.security.service;


import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.security.payload.request.LoginRequest;
import com.treetoplodge.treetoplodge_api.security.payload.request.RefreshTokenRequest;
import com.treetoplodge.treetoplodge_api.security.payload.request.RegisterRequest;
import com.treetoplodge.treetoplodge_api.security.payload.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse login(LoginRequest req);
    User register(RegisterRequest req);
    AuthResponse refreshToken(RefreshTokenRequest req);
}
