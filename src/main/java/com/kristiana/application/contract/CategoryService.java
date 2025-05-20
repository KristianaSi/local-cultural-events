package com.kristiana.application.contract;


import com.kristiana.application.dto.AddDtoCategory;
import com.kristiana.application.dto.UpdateDtoCategory;
import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.Event;
import com.kristiana.domain.entities.Category;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Інтерфейс для управління сутностями категорій, включаючи операції з пов'язаними аудіокнигами.
 */
public interface CategoryService {

    /**
     * Створює нову категорію.
     *
     * @param category категорія для створення
     * @return створена категорія
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     * @throws ValidationException     якщо порушено бізнес-правила (наприклад, дублювання категорії)
     */
    Category create(AddDtoCategory category);

    /**
     * Оновлює існуючу категорію.
     *
     * @param id       ідентифікатор категорії для оновлення
     * @param category оновлені дані категорії
     * @return оновлена категорія
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     * @throws ValidationException     якщо порушено бізнес-правила
     */
    Category update(UUID id, UpdateDtoCategory category);

    /**
     * Видаляє категорію, якщо вона не пов'язана з аудіокнигами.
     *
     * @param id ідентифікатор категорії для видалення
     * @throws DatabaseAccessException якщо виникає помилка при роботі з базою даних
     * @throws ValidationException     якщо категорія пов'язана з аудіокнигами
     */
    void delete(UUID id);

    /**
     * Знаходить категорію за ідентифікатором.
     *
     * @param id ідентифікатор категорії
     * @return Optional з категорією, якщо знайдено
     */
    Optional<Category> findById(UUID id);

    /**
     * Знаходить всі категорії з пагінацією.
     *
     * @param offset зміщення для пагінації
     * @param limit  кількість записів для отримання
     * @return список категорій
     */
    List<Category> findAll(int offset, int limit);

    /**
     * Знаходить категорії за назвою.
     *
     * @param name назва категорії
     * @return список категорій
     */
    List<Category> findByName(String name);

    /**
     * Знаходить категорії за частковою відповідністю назви.
     *
     * @param partialName часткова назва категорії
     * @return список категорій
     */
    List<Category> findByPartialName(String partialName);

    /**
     * Знаходить аудіокниги, пов'язані з категорією.
     *
     * @param categoryId ідентифікатор категорії
     * @return список аудіокниг
     */
    List<Event> findEventByCategoryId(UUID categoryId);

    /**
     * Знаходить категорії, пов'язані з аудіокнигою.
     *
     * @param EventId ідентифікатор аудіокниги
     * @return список категорій
     */
    List<Category> findByEventId(UUID EventId);

    /**
     * Підраховує кількість аудіокниг, пов'язаних з категорією.
     *
     * @param categoryId ідентифікатор категорії
     * @return кількість аудіокниг
     */
    long countEventByCategoryId(UUID categoryId);

    /**
     * Перевіряє існування категорії за назвою.
     *
     * @param name назва категорії
     * @return true, якщо категорія існує
     */
    boolean existsByName(String name);

    private void validateCategory(Category category) {
        if (category == null) {
            throw new ValidationException("Категорія не може бути null.");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Назва категорії не може бути null або порожньою.");
        }
    }

}
