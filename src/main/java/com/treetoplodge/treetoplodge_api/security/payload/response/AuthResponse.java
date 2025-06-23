package com.treetoplodge.treetoplodge_api.security.payload.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
