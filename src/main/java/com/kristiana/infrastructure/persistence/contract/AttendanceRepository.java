package com.kristiana.infrastructure.persistence.contract;

import com.kristiana.domain.entities.Attendance;
import com.kristiana.domain.entities.Category;
import com.kristiana.infrastructure.persistence.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Інтерфейс репозиторію для специфічних операцій з відвідуванням подій.
 */
public interface AttendanceRepository extends Repository<Attendance, UUID> {

    /**
     * Пошук відвідувань за назвою.
     *
     * @param name назва
     * @return список записів відвідування
     */
    List<Attendance> findByName(String name);

    /**
     * Пошук відвідувань за ідентифікатором події.
     *
     * @param eventId ідентифікатор події
     * @return список записів відвідування
     */
    List<Attendance> findByEventId(UUID eventId);

    /**
     * Пошук відвідувань за ідентифікатором користувача.
     *
     * @param userId ідентифікатор користувача
     * @return список записів відвідування
     */
    List<Attendance> findByUserId(UUID userId);

    /**
     * Пошук відвідувань за статусом.
     *
     * @param status статус (true або false)
     * @return список записів відвідування
     */
    List<Attendance> findByStatus(boolean status);

    /**
     * Пошук відвідувань за частковою відповідністю назви.
     *
     * @param partialName часткова назва
     * @return список записів відвідування
     */
    List<Attendance> findByNameContainingIgnoreCase(String partialName);

    /**
     * Підрахунок відвідувань для певної події.
     *
     * @param eventId ідентифікатор події
     * @return кількість відвідувань
     */
    long countByEventId(UUID eventId);

    /**
     * Перевірка існування запису за назвою.
     *
     * @param name назва
     * @return true, якщо такий запис існує
     */
    boolean existsByName(String name);
}
