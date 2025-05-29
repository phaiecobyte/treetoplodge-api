package com.treetoplodge.treetoplodge_api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "refresh token is required")
    private String refreshToken;
}