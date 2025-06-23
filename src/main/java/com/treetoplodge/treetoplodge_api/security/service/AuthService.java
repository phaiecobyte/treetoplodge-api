package com.phaiecobyte.spring_security.service;

import com.phaiecobyte.spring_security.dto.JwtAuthenticationRes;
import com.phaiecobyte.spring_security.dto.LoginReq;
import com.phaiecobyte.spring_security.dto.RefreshTokenReq;
import com.phaiecobyte.spring_security.dto.RegisterReq;
import com.phaiecobyte.spring_security.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    JwtAuthenticationRes login(LoginReq req);
    User register(RegisterReq req);
    JwtAuthenticationRes refreshToken(RefreshTokenReq req);
}
