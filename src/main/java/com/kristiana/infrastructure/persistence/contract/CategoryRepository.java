package com.kristiana.infrastructure.persistence.contract;

import com.kristiana.domain.entities.Event;
import com.kristiana.domain.entities.Category;
import com.kristiana.infrastructure.persistence.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Інтерфейс репозиторію для специфічних операцій з категоріями.
 */
public interface CategoryRepository extends Repository<Category, UUID> {

    /**
     * Пошук категорії за назвою.
     *
     * @param name назва категорії
     * @return список категорій
     */
    List<Category> findByName(String name);

    /**
     * Пошук подій за ідентифікатором категорій.
     *
     * @param categoryId ідентифікатор категорій
     * @return список подій
     */
    List<Event> findEventsByCategoryId(UUID categoryId);

    /**
     * Пошук категорій за ідентифікатором подій.
     *
     * @param eventId ідентифікатор подій
     * @return список категорій
     */
    List<Category> findByEventId(UUID eventId);

    /**
     * Пошук категорій за частковою відповідністю назви.
     *
     * @param partialName часткова назва категорії
     * @return список категорій
     */
    List<Category> findByPartialName(String partialName);

    /**
     * Підрахунок подій для категорій.
     *
     * @param categoryId ідентифікатор категорії
     * @return кількість подій
     */
    long countEventByCategoryId(UUID categoryId);

    /**
     * Перевірка існування категорії за назвою.
     *
     * @param name назва категорії
     * @return true, якщо категорія існує
     */
    boolean existsByName(String name);
}