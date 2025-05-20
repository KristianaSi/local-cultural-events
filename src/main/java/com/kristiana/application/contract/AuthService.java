package com.kristiana.application.contract;

import com.kristiana.application.exception.AuthException;
import com.kristiana.domain.entities.User;

public interface AuthService {
    // Core authentication methods
    boolean login(String username, String password) throws AuthException;
    void logout() throws AuthException;
    User getCurrentUser() throws AuthException;
    boolean isAuthenticated();

}
