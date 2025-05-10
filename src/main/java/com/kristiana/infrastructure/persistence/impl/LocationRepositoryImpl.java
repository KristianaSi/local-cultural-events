package com.kristiana.infrastructure.persistence.impl;

import com.kristiana.domain.entities.Location;
import com.kristiana.infrastructure.persistence.GenericRepository;
import com.kristiana.infrastructure.persistence.contract.LocationRepository;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Filter;

/**
 * Реалізація репозиторію для специфічних операцій з локаціями.
 */
@Repository
public class LocationRepositoryImpl extends GenericRepository<Location, UUID> implements LocationRepository {

    /**
     * Конструктор репозиторію.
     *
     * @param connectionPool з'єднання до бази даних
     */
    public LocationRepositoryImpl(ConnectionPool connectionPool) {
        super(connectionPool, Location.class, "locations");
    }

    /**
     * Пошук локацій за назвою.
     *
     * @param name назва локації
     * @return список локацій
     */
    @Override
    public List<Location> findByName(String name) {
        return findByField("name", name);
    }

    /**
     * Пошук локацій за частковою відповідністю назви.
     *
     * @param partialName часткова назва
     * @return список локацій
     */
    @Override
    public List<Location> findByNameContainingIgnoreCase(String partialName) {
        return findAll(
            (whereClause, params) -> {
                whereClause.add("name ILIKE ?");
                params.add("%" + partialName + "%");
            },
            null, true, 0, Integer.MAX_VALUE
        );
    }

    /**
     * Пошук локацій за адресою.
     *
     * @param address адреса локації
     * @return список локацій
     */
    @Override
    public List<Location> findByAddress(String address) {
        return findByField("address", address);
    }

    /**
     * Перевірка існування локації за назвою.
     *
     * @param name назва локації
     * @return true, якщо така локація існує
     */
    @Override
    public boolean existsByName(String name) {
        Filter filter = (whereClause, params) -> {
            whereClause.add("name = ?");
            params.add(name);
        };
        return count(filter) > 0;
    }

    /**
     * Перевірка існування локації за адресою.
     *
     * @param address адреса локації
     * @return true, якщо така адреса існує
     */
    @Override
    public boolean existsByAddress(String address) {
        Filter filter = (whereClause, params) -> {
            whereClause.add("address = ?");
            params.add(address);
        };
        return count(filter) > 0;
    }
}
