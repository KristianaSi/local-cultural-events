package com.kristiana.application.contract;

import com.kristiana.application.dto.AddDtoUser;
import com.kristiana.domain.entities.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User findById(UUID id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    List<User> findByPartialUsername(String partialUsername);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User create(AddDtoUser addDtoUser);
}