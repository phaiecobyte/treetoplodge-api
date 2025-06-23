package com.treetoplodge.treetoplodge_api.security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    @NotBlank(message = "surname is required") @Size(max = 50,message = "max length surname is 50")
    private String surname;

    @NotBlank(message = "forename is required") @Size(max = 50,message = "max length forename is 50")
    private String forename;

    @NotNull
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @Size(max = 100) @Email
    private String email;

    private Set<String> roles;

    @NotBlank(message = "password is required")
    @Size(min = 3, max = 40, message = "password min length is 3 max length is 40")
    private String password;

}