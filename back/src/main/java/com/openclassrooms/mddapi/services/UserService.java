package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserUpdateRequest;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Read sensitive settings from environment variables with defaults
    @Value("${app.security.min-password-length:8}")
    private int minPasswordLength;
    
    @Value("${app.security.password-check-enabled:true}")
    private boolean passwordCheckEnabled;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }    
      @Override
    public User findByUsername(String username) {
        System.out.println("Looking up user by username: " + username);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.err.println("User not found in database: " + username);
                return new AuthException("User not found: " + username);
            });
    }
    
    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        System.out.println("Looking up user by username or email: " + usernameOrEmail);
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> {
                System.err.println("User not found in database with username or email: " + usernameOrEmail);
                return new AuthException("Nom d'utilisateur ou email non trouvé: " + usernameOrEmail);
            });
    }
    
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new AuthException("Utilisateur non trouvé"));
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public User updateUser(Long id, UserUpdateRequest updateRequest) {
        User existingUser = findById(id);
        
        // Vérifier si le nouveau nom d'utilisateur est déjà pris (sauf s'il s'agit du même nom)
        if (updateRequest.getUsername() != null && 
            !updateRequest.getUsername().equals(existingUser.getUsername()) && 
            !isUsernameAvailable(updateRequest.getUsername())) {
            throw new AuthException("Nom d'utilisateur déjà utilisé");
        }
        
        // Vérifier si le nouvel email est déjà pris (sauf s'il s'agit du même email)
        if (updateRequest.getEmail() != null && 
            !updateRequest.getEmail().equals(existingUser.getEmail()) && 
            !isEmailAvailable(updateRequest.getEmail())) {
            throw new AuthException("Email déjà utilisé");
        }
        
        // Mettre à jour les champs non nuls
        if (updateRequest.getUsername() != null) {
            existingUser.setUsername(updateRequest.getUsername());
        }
        
        if (updateRequest.getEmail() != null) {
            existingUser.setEmail(updateRequest.getEmail());
        }
        
        // Check password using environment variable configuration
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            // Validate password if checks are enabled via environment variable
            if (passwordCheckEnabled && updateRequest.getPassword().length() < minPasswordLength) {
                throw new AuthException("Le mot de passe doit contenir au moins " + minPasswordLength + " caractères");
            }
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
}