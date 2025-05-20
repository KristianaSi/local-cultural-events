package com.kristiana.application.impl;

import com.password4j.Password;
import com.kristiana.application.contract.PasswordService;
import org.springframework.stereotype.Service;

// TODO: make these classes private and realise the using of the methods according to that
@Service
public class PasswordServiceImpl implements PasswordService {

    @Override
    public String hash(String plainPassword) {
        return Password.hash(plainPassword).withBcrypt().getResult();
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword) {
        return Password.check(plainPassword, hashedPassword).withBcrypt();
    }
}
