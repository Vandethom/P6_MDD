package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserUpdateRequest;
import com.openclassrooms.mddapi.models.User;

public interface IUserService {
    User    findByUsername(String username);
    User    findById(Long id);
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);
    User    createUser(User user);
    User    updateUser(Long id, UserUpdateRequest updateRequest);
}