package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.security.AuthException;
import com.openclassrooms.mddapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final IUserService    userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    @Autowired
    public AuthService(IUserService   userService,
                      PasswordEncoder passwordEncoder,
                      JwtUtil         jwtUtil
                      ) {
        this.userService     = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // Get user by username or email
        User user = userService.findByUsernameOrEmail(loginRequest.getUsernameOrEmail());
        
        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException("Nom d'utilisateur ou mot de passe incorrect");
        }
        
        // Generate token
        String token = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, user.getUsername(), user.getId());
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if username and email are available
        if (!userService.isUsernameAvailable(registerRequest.getUsername())) {
            throw new AuthException("Nom d'utilisateur déjà utilisé");
        }
        
        if (!userService.isEmailAvailable(registerRequest.getEmail())) {
            throw new AuthException("Email déjà utilisé");
        }        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .build();
        
        User savedUser = userService.createUser(user);
        String token   = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, savedUser.getUsername(), savedUser.getId());
    }
    
    @Override
    public boolean validateToken(
        String token, 
        String username
        ) {
            return jwtUtil.validateToken(token, username);
        }

    /**
     * Extrait l'ID de l'utilisateur à partir de l'objet Authentication
     * @param authentication l'objet Authentication courant
     * @return l'ID de l'utilisateur authentifié
     */
    public Long getUserIdFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return user.getId();
    }
}