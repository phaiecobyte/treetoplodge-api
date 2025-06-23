package com.treetoplodge.treetoplodge_api.security.service.impl;

import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import com.treetoplodge.treetoplodge_api.security.payload.request.LoginRequest;
import com.treetoplodge.treetoplodge_api.security.payload.request.RefreshTokenRequest;
import com.treetoplodge.treetoplodge_api.security.payload.request.RegisterRequest;
import com.treetoplodge.treetoplodge_api.security.payload.response.AuthResponse;
import com.treetoplodge.treetoplodge_api.security.service.AuthService;
import com.treetoplodge.treetoplodge_api.security.service.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword())
        );

        var user = userRepository.findByEmail(req.getUsername())
                .orElseThrow(()-> new IllegalArgumentException("Invalid username or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setAccessToken(jwt);
        authResponse.setRefreshToken(refreshToken);

        return authResponse;
    }

    @Override
    public User register(RegisterRequest req) {
        User user = new User();

        return null;
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest req) {
        return null;
    }
}
