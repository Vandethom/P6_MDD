package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.User;

public interface IUserService {
    User    findByUsername(String username);
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);
    User    createUser(User user);
}