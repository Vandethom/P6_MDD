package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;
    
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;
    
    // Le mot de passe est optionnel pour la mise à jour
    @StrongPassword
    private String password;
}
