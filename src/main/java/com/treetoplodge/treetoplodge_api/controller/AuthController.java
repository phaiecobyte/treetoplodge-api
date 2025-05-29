package com.treetoplodge.treetoplodge_api.controller;

import com.treetoplodge.treetoplodge_api.Service.impl.RefreshTokenServiceImpl;
import com.treetoplodge.treetoplodge_api.exception.ApiResponse;
import com.treetoplodge.treetoplodge_api.exception.AppException;
import com.treetoplodge.treetoplodge_api.model.RefreshToken;
import com.treetoplodge.treetoplodge_api.model.Role;
import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.payload.request.LoginRequest;
import com.treetoplodge.treetoplodge_api.payload.request.RefreshTokenRequest;
import com.treetoplodge.treetoplodge_api.payload.request.SignupRequest;
import com.treetoplodge.treetoplodge_api.payload.response.JwtResponse;
import com.treetoplodge.treetoplodge_api.payload.response.MessageResponse;
import com.treetoplodge.treetoplodge_api.payload.response.TokenRefreshResponse;
import com.treetoplodge.treetoplodge_api.repository.RoleRepository;
import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import com.treetoplodge.treetoplodge_api.security.jwt.JwtUtils;
import com.treetoplodge.treetoplodge_api.security.service.UserDetailsImpl;
import com.treetoplodge.treetoplodge_api.util.Generator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Authentication request for user: {}", loginRequest.getUsername());
        
        try {
            // Check if user exists before attempting authentication
            if (!userRepository.existsByUsername(loginRequest.getUsername())) {
                log.warn("Login attempt for non-existent user: {}", loginRequest.getUsername());
                return ResponseEntity.status(401).body(new MessageResponse("Error: Username not found"));
            }
            
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(authentication);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserId());

            log.info("User authenticated successfully: {}", userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    refreshToken.getToken(),
                    userDetails.getId(),
                    userDetails.getUserId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getFullName(),
                    userDetails.getPhoneNumber(),
                    roles));
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body(new MessageResponse("Error: Incorrect username or password"));
        } catch (Exception e) {
            log.error("Error during authentication: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new MessageResponse("Error: Internal server error"));
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        try {
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                    })
                    .orElseThrow(() -> AppException.badRequest("Refresh token is not in database!"));
        } catch (AppException e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(e.getHttpStatus().value())
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during token refresh: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(new MessageResponse("Error refreshing token: " + e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            // Create new user's account
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setFullName(signUpRequest.getFullName());
            user.setPhoneNumber(signUpRequest.getPhoneNumber());
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setCreatedBy("self-registration");
            user.setUserId(Generator.generateUserId());

            Set<String> strRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null || strRoles.isEmpty()) {
                Role customerRole = roleRepository.findByName(Role.ERole.ROLE_CUSTOMER)
                        .orElseThrow(() -> new RuntimeException("Error: Customer Role is not found."));
                roles.add(customerRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role.toLowerCase()) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "shop":
                            Role shopRole = roleRepository.findByName(Role.ERole.ROLE_SHOP)
                                    .orElseThrow(() -> new RuntimeException("Error: Shop Role is not found."));
                            roles.add(shopRole);
                            break;
                        default:
                            Role customerRole = roleRepository.findByName(Role.ERole.ROLE_CUSTOMER)
                                    .orElseThrow(() -> new RuntimeException("Error: Customer Role is not found."));
                            roles.add(customerRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);

            log.info("User registered successfully with username: {}", signUpRequest.getUsername());
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new MessageResponse("Error: Registration failed - " + e.getMessage()));
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            return refreshTokenService.findByToken(refreshToken)
                    .map(token -> {
                        // Use the correct method with the correct ID type
                        refreshTokenService.deleteByUser(token.getUser().getId());
                        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
                    })
                    .orElse(ResponseEntity.ok(new MessageResponse("Log out successful!")));
                    
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage(), e);
            // Still return success to client as they're logging out anyway
            return ResponseEntity.ok(new MessageResponse("Log out successful!"));
        }
    }
}