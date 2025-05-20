package com.kristiana.application.contract;

import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.Location;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Інтерфейс для управління сутностями локацій.
 */
public interface LocationService {

    /**
     * Створює нову локацію.
     *
     * @param location локація для створення
     * @return створена локація
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     * @throws ValidationException     якщо порушено бізнес-правила (наприклад, дублювання локації)
     */
    Location create(Location location) throws ValidationException, DatabaseAccessException;

    /**
     * Оновлює існуючу локацію.
     *
     * @param id       ідентифікатор локації для оновлення
     * @param location оновлені дані локації
     * @return оновлена локація
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     * @throws ValidationException     якщо порушено бізнес-правила
     */
    Location update(UUID id, Location location) throws ValidationException, DatabaseAccessException;

    /**
     * Видаляє локацію.
     *
     * @param id ідентифікатор локації для видалення
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     * @throws ValidationException     якщо локація не знайдена
     */
    void delete(UUID id) throws DatabaseAccessException, ValidationException;

    /**
     * Знаходить локацію за ідентифікатором.
     *
     * @param id ідентифікатор локації
     * @return Optional з локацією, якщо знайдено
     */
    Optional<Location> findById(UUID id);

    /**
     * Знаходить всі локації з пагінацією.
     *
     * @param offset зміщення для пагінації
     * @param limit  кількість записів для отримання
     * @return список локацій
     */
    List<Location> findAll(int offset, int limit);

    /**
     * Знаходить локації за частковою відповідністю назви.
     *
     * @param partialName часткова назва локації
     * @return список локацій
     */
    List<Location> findByPartialName(String partialName);
}

