package com.treetoplodge.treetoplodge_api.security.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "password is required")
    private String password;
}