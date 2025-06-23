package com.treetoplodge.treetoplodge_api.security.service.impl;

import com.treetoplodge.treetoplodge_api.model.Role;
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
import java.util.Set;
import java.util.HashSet;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @Override
    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getPhoneNumber(),req.getPassword())
        );

        var user = userRepository.findByPhoneNumber(req.getPhoneNumber())
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
        user.setSurname(req.getSurname());
        user.setForename(req.getForename());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setEmail(req.getEmail());

        Set<Role> ROLE_CUSTOMER = new HashSet<>();
        user.setRoles(ROLE_CUSTOMER);
        
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest req) {
        String email = jwtService.extractUsername(req.getRefreshToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        if(jwtService.isTokenValid(req.getRefreshToken(),user)){
            var jwt = jwtService.generateToken(user);

            AuthResponse authResponse = new AuthResponse();

            authResponse.setAccessToken(jwt);
            authResponse.setRefreshToken(req.getRefreshToken());
            return authResponse;
        }
        return null;
    }
}