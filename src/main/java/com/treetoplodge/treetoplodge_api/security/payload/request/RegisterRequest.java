package com.treetoplodge.treetoplodge_api.security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50,message = "username min length 3 and max length 50")
    private String username;

    @NotBlank(message = "email is required")
    @Size(max = 100)
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 40, message = "password min length is 6 max length is 40")
    private String password;

    @NotBlank(message = "full name is required")
    private String fullName;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;
}