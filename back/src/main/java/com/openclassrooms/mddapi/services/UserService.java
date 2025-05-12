package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserUpdateRequest;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }    @Override
    public User findByUsername(String username) {
        System.out.println("Looking up user by username: " + username);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.err.println("User not found in database: " + username);
                return new AuthException("User not found: " + username);
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
        
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
}