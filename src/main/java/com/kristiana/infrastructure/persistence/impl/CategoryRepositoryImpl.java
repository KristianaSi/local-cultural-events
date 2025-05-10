package com.kristiana.infrastructure.persistence.impl;

import com.kristiana.domain.entities.Category;
import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.GenericRepository;
import com.kristiana.infrastructure.persistence.contract.CategoryRepository;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Реалізація репозиторію для специфічних операцій з категоріями.
 */
@Repository
public class CategoryRepositoryImpl extends GenericRepository<Category, UUID> implements CategoryRepository {

    public CategoryRepositoryImpl(ConnectionPool connectionPool) {
        super(connectionPool, Category.class, "categories");
    }

    @Override
    public List<Category> findByName(String name) {
        return findByField("name", name);
    }

    @Override
    public List<Event> findEventsByCategoryId(UUID categoryId) {
        String sql = "SELECT * FROM events WHERE category_id = ?";
        return executeQuery(sql, stmt -> stmt.setObject(1, categoryId), resultSet -> {
            Event event = new Event();
            event.setId(UUID.fromString(resultSet.getString("id")));
            event.setName(resultSet.getString("name"));
            event.setDescription(resultSet.getString("description"));
            event.setDate(resultSet.getDate("date").toLocalDate());
            event.setLocationId(UUID.fromString(resultSet.getString("location_id")));
            event.setCategoryId(UUID.fromString(resultSet.getString("category_id")));
            return event;
        });
    }

    @Override
    public List<Category> findByEventId(UUID eventId) {
        String sql = "SELECT c.* FROM categories c " +
            "JOIN events e ON c.id = e.category_id " +
            "WHERE e.id = ?";
        return executeQuery(sql, stmt -> stmt.setObject(1, eventId), resultSet -> {
            Category category = new Category();
            category.setId(UUID.fromString(resultSet.getString("id")));
            category.setName(resultSet.getString("name"));
            category.setDescription(resultSet.getString("description"));
            return category;
        });
    }


    @Override
    public List<Category> findByPartialName(String partialName) {
        return findAll(
            (whereClause, params) -> {
                whereClause.add("name ILIKE ?");
                params.add("%" + partialName + "%");
            },
            null, true, 0, Integer.MAX_VALUE
        );
    }

    @Override
    public long countAudiobooksByGenreId(UUID categoryId) {
        String sql = "SELECT COUNT(*) FROM events WHERE category_id = ?";
        return executeCountQuery(sql, List.of(categoryId));
    }

    @Override
    public boolean existsByName(String name) {
        return count((whereClause, params) -> {
            whereClause.add("name = ?");
            params.add(name);
        }) > 0;
    }
}
