package com.kristiana.application.contract;


import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.Attendance;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Інтерфейс для управління сутностями відвідуваності.
 */
public interface AttendanceService {

    Attendance create(Attendance attendance) throws ValidationException, DatabaseAccessException;

    Attendance update(UUID id, Attendance attendance) throws ValidationException, DatabaseAccessException;

    void delete(UUID id) throws DatabaseAccessException, ValidationException;

    Optional<Attendance> findById(UUID id);

    List<Attendance> findAll(int offset, int limit);

    List<Attendance> findByEventId(UUID eventId);

}
