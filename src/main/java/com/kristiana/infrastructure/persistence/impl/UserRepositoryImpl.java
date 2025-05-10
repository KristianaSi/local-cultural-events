package com.kristiana.infrastructure.persistence.impl;
import com.kristiana.domain.entities.User;
import com.kristiana.infrastructure.persistence.GenericRepository;
import com.kristiana.infrastructure.persistence.contract.UserRepository;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Реалізація репозиторію для специфічних операцій з користувачами.
 */
@Repository
public class UserRepositoryImpl extends GenericRepository<User, UUID> implements UserRepository {

    /**
     * Конструктор репозиторію.
     *
     * @param connectionPool пул з'єднань до бази даних
     */
    public UserRepositoryImpl(ConnectionPool connectionPool) {
        super(connectionPool, User.class, "users");
    }

    /**
     * Пошук користувача за ім’ям користувача.
     *
     * @param username ім’я користувача
     * @return список користувачів
     */
    @Override
    public List<User> findByUsername(String username) {
        return findByField("username", username);
    }

    /**
     * Пошук користувача за електронною поштою.
     *
     * @param email електронна пошта
     * @return список користувачів
     */
    @Override
    public List<User> findByEmail(String email) {
        return findByField("email", email);
    }





    /**
     * Пошук користувачів за частковою відповідністю імені.
     *
     * @param partialUsername часткове ім’я користувача
     * @return список користувачів
     */
    @Override
    public List<User> findByPartialUsername(String partialUsername) {
        return findAll(
            (whereClause, params) -> {
                whereClause.add("username ILIKE ?");
                params.add("%" + partialUsername + "%");
            },
            null, true, 0, Integer.MAX_VALUE
        );
    }


    /**
     * Перевірка існування користувача за ім’ям.
     *
     * @param username ім’я користувача
     * @return true, якщо користувач існує
     */
    @Override
    public boolean existsByUsername(String username) {
        Filter filter = (whereClause, params) -> {
            whereClause.add("username = ?");
            params.add(username);
        };
        return count(filter) > 0;
    }

    /**
     * Перевірка існування користувача за електронною поштою.
     *
     * @param email електронна пошта
     * @return true, якщо користувач існує
     */
    @Override
    public boolean existsByEmail(String email) {
        Filter filter = (whereClause, params) -> {
            whereClause.add("email = ?");
            params.add(email);
        };
        return count(filter) > 0;
    }


}
