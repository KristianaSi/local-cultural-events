package com.kristiana.application.impl;

import com.kristiana.application.contract.UserService;
import com.kristiana.application.dto.AddDtoUser;
import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.User;
import com.kristiana.application.contract.PasswordService;
import com.kristiana.infrastructure.persistence.contract.UserRepository;
import com.kristiana.infrastructure.persistence.exception.EntityNotFoundException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;
    private final PasswordService passwordService;

    public UserServiceImpl(UserRepository userRepository,
        PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = userRepository.findByUsername(username);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return users.get(0);
    }

    @Override
    public User findByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return users.get(0);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByPartialUsername(String partialUsername) {
        return userRepository.findByPartialUsername(partialUsername);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User create(AddDtoUser addDtoUser) {
        var violations = validator.validate(addDtoUser);
        if (!violations.isEmpty()) {
            throw ValidationException.create("user creation", violations);
        }

        User user = new User(
            null,
            addDtoUser.username(),
            addDtoUser.email(),
            passwordService.hash(addDtoUser.password()),
            addDtoUser.role()
        );

        return userRepository.save(user);
    }
}
