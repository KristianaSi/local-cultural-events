package com.kristiana.application.contract;

import org.springframework.stereotype.Component;

@Component
public interface PasswordService {
    String hash(String plainPassword);
    boolean verify(String plainPassword, String hashedPassword);
}
