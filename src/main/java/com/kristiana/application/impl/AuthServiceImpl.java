package com.kristiana.application.impl;

import com.kristiana.application.contract.AuthService;
import com.kristiana.application.contract.PasswordService;
import com.kristiana.application.exception.AuthException;
import com.kristiana.domain.entities.User;
import com.kristiana.infrastructure.persistence.contract.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private User currentUser;

    public AuthServiceImpl(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public boolean login(String username, String password) throws AuthException {
        if (currentUser != null) {
            throw new AuthException("Already logged in as: " + currentUser.getUsername());
        }

        User user =
            userRepository.findByUsername(username).stream()
                .findFirst()
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!passwordService.verify(password, user.getPasswordHash())) {
            return false;
        }

        currentUser = user;
        return true;
    }

    @Override
    public void logout() throws AuthException {
        if (currentUser == null) {
            throw new AuthException("No active session");
        }
        currentUser = null;
    }

    @Override
    public User getCurrentUser() throws AuthException {
        if (currentUser == null) {
            throw new AuthException("Not authenticated");
        }
        return currentUser;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null;
    }


}