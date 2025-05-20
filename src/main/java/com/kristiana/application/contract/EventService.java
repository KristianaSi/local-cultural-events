package com.kristiana.application.contract;

import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Інтерфейс для управління сутностями подій.
 */
public interface EventService {

    /**
     * Створює нову подію.
     *
     * @param event подія для створення
     * @return створена подія
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     */
    Event create(Event event);

    /**
     * Оновлює існуючу подію.
     *
     * @param id ідентифікатор події
     * @param event оновлені дані події
     * @return оновлена подія
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     */
    Event update(UUID id, Event event);

    /**
     * Видаляє подію.
     *
     * @param id ідентифікатор події
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     */
    void delete(UUID id);

    /**
     * Повертає подію за її ідентифікатором.
     *
     * @param id ідентифікатор події
     * @return Optional з подією, якщо знайдено
     */
    Optional<Event> findById(UUID id);

    /**
     * Повертає список усіх подій з пагінацією.
     *
     * @param offset зміщення для пагінації
     * @param limit кількість записів для отримання
     * @return список подій
     */
    List<Event> findAll(int offset, int limit);

    /**
     * Знаходить події за назвою.
     *
     * @param name назва події
     * @return список подій
     */
    List<Event> findByName(String name);

    /**
     * Знаходить події за частковою відповідністю назви.
     *
     * @param partialName часткова назва події
     * @return список подій
     */
    List<Event> findByPartialName(String partialName);
}
