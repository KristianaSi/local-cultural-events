package com.kristiana.infrastructure.persistence.impl;

import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.GenericRepository;
import com.kristiana.infrastructure.persistence.contract.EventRepository;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.logging.Filter;

/**
 * Реалізація репозиторію для специфічних операцій з подіями.
 */
@Repository
public class EventRepositoryImpl extends GenericRepository<Event, UUID> implements EventRepository {

    /**
     * Конструктор репозиторію подій.
     *
     * @param connectionPool  з'єднання до бази даних
     */
    public EventRepositoryImpl(ConnectionPool connectionPool) {
        super(connectionPool, Event.class, "events");
    }

    @Override
    public List<Event> findByName(String name) {
        return findByField("name", name);
    }

    @Override
    public List<Event> findByNameContainingIgnoreCase(String partialName) {
        return List.of();
    }

    @Override
    public List<Event> findByPartialName(String partialName) {
        return findAll(
            (whereClause, params) -> {
                whereClause.add("name ILIKE ?");
                params.add("%" + partialName + "%");
            },
            null, true, 0, Integer.MAX_VALUE
        );
    }

    @Override
    public List<Event> findByDate(LocalDate date) {
        return findByField("date", date);
    }

    @Override
    public List<Event> findByLocationId(UUID locationId) {
        return findByField("location_id", locationId);
    }

    @Override
    public List<Event> findByCategoryId(UUID categoryId) {
        return findByField("category_id", categoryId);
    }

    @Override
    public long countByCategoryId(UUID categoryId) {
        return count((whereClause, params) -> {
            whereClause.add("category_id = ?");
            params.add(categoryId);
        });
    }

    @Override
    public boolean existsByName(String name) {
        return count((whereClause, params) -> {
            whereClause.add("name = ?");
            params.add(name);
        }) > 0;
    }
}
