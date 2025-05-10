package com.kristiana.infrastructure.persistence.contract;

import com.kristiana.domain.entities.Location;
import com.kristiana.infrastructure.persistence.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Інтерфейс репозиторію для специфічних операцій з локаціями.
 */
public interface LocationRepository extends Repository<Location, UUID> {

    /**
     * Пошук локацій за назвою.
     *
     * @param name назва локації
     * @return список локацій
     */
    List<Location> findByName(String name);

    /**
     * Пошук локацій за частковою відповідністю назви.
     *
     * @param partialName часткова назва
     * @return список локацій
     */
    List<Location> findByNameContainingIgnoreCase(String partialName);

    /**
     * Пошук локацій за адресою.
     *
     * @param address адреса
     * @return список локацій
     */
    List<Location> findByAddress(String address);

    /**
     * Перевірка існування локації за назвою.
     *
     * @param name назва локації
     * @return true, якщо така локація існує
     */
    boolean existsByName(String name);

    /**
     * Перевірка існування локації за адресою.
     *
     * @param address адреса локації
     * @return true, якщо така адреса існує
     */
    boolean existsByAddress(String address);
}

