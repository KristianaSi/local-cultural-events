package com.kristiana.infrastructure.persistence.contract;

import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Інтерфейс репозиторію для специфічних операцій з подіями.
 */
public interface EventRepository extends Repository<Event, UUID> {

    /**
     * Пошук подій за назвою.
     *
     * @param name назва події
     * @return список подій
     */
    List<Event> findByName(String name);

    /**
     * Пошук подій за частковою відповідністю назви.
     *
     * @param partialName часткова назва
     * @return список подій
     */
    List<Event> findByNameContainingIgnoreCase(String partialName);

    /**
     * Пошук подій за датою.
     *
     * @param date дата події
     * @return список подій
     */
    List<Event> findByDate(LocalDate date);

    /**
     * Пошук подій за ідентифікатором локації.
     *
     * @param locationId ідентифікатор локації
     * @return список подій
     */
    List<Event> findByLocationId(UUID locationId);

    /**
     * Пошук подій за ідентифікатором категорії.
     *
     * @param categoryId ідентифікатор категорії
     * @return список подій
     */
    List<Event> findByCategoryId(UUID categoryId);

    /**
     * Підрахунок подій для певної категорії.
     *
     * @param categoryId ідентифікатор категорії
     * @return кількість подій
     */
    long countByCategoryId(UUID categoryId);

    /**
     * Перевірка існування події за назвою.
     *
     * @param name назва події
     * @return true, якщо подія існує
     */
    boolean existsByName(String name);
}

