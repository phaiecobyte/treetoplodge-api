package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.exception.ApiResponse;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.security.payload.request.LoginRequest;
import com.treetoplodge.treetoplodge_api.security.payload.request.RefreshTokenRequest;
import com.treetoplodge.treetoplodge_api.security.payload.request.RegisterRequest;
import com.treetoplodge.treetoplodge_api.security.payload.response.AuthResponse;
import com.treetoplodge.treetoplodge_api.security.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest req) {
        try {
            User registeredUser = authService.register(req);
            log.info("User registered successfully: {}", registeredUser.getPhoneNumber());
            return ApiResponse.success(registeredUser);
        } catch (AppException e) {
            log.error("Application error during registration: {}", e.getMessage());
            return ApiResponse.error(e);
        } catch (Exception e) {
            log.error("Unexpected error during registration: {}", e.getMessage());
            return ApiResponse.error(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest req){
        return ResponseEntity.ok(authService.refreshToken(req));
    }
}