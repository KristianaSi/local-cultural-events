package com.kristiana.infrastructure.persistence.contract;


import com.kristiana.domain.entities.User;
import com.kristiana.infrastructure.persistence.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Інтерфейс репозиторію для специфічних операцій з користувачами.
 */
public interface UserRepository extends Repository<User, UUID> {

    /**
     * Пошук користувача за ім’ям користувача.
     *
     * @param username ім’я користувача
     * @return список користувачів
     */
    List<User> findByUsername(String username);

    /**
     * Пошук користувача за електронною поштою.
     *
     * @param email електронна пошта
     * @return список користувачів
     */
    List<User> findByEmail(String email);



    /**
     * Пошук користувачів за частковою відповідністю імені.
     *
     * @param partialUsername часткове ім’я користувача
     * @return список користувачів
     */
    List<User> findByPartialUsername(String partialUsername);



    /**
     * Перевірка існування користувача за ім’ям.
     *
     * @param username ім’я користувача
     * @return true, якщо користувач існує
     */
    boolean existsByUsername(String username);

    /**
     * Перевірка існування користувача за електронною поштою.
     *
     * @param email електронна пошта
     * @return true, якщо користувач існує
     */
    boolean existsByEmail(String email);
}
