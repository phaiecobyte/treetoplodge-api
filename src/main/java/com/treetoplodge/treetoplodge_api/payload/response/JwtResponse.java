package com.treetoplodge.treetoplodge_api.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long id;
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, Long id, String userId, String username,
                       String email, String fullName, String phoneNumber, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}