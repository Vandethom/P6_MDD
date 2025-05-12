package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import org.springframework.security.core.Authentication;

public interface IAuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);
    boolean      validateToken(String token, String username);
    Long         getUserIdFromAuthentication(Authentication authentication);
}