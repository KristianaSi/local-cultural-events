package com.kristiana.application.impl;

import com.kristiana.application.contract.AttendanceService;
import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.Attendance;
import com.kristiana.infrastructure.persistence.PersistenceContext;
import com.kristiana.infrastructure.persistence.contract.AttendanceRepository;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final PersistenceContext persistenceContext;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
        PersistenceContext persistenceContext) {
        this.attendanceRepository = attendanceRepository;
        this.persistenceContext = persistenceContext;
    }

    @Override
    public Attendance create(Attendance attendance) throws ValidationException, DatabaseAccessException {
        validateAttendance(attendance);

        if (attendance.getId() == null) {
            attendance.setId(UUID.randomUUID());
        }


        persistenceContext.registerNew(attendance);
        persistenceContext.commit();
        return attendance;
    }

    @Override
    public Attendance update(UUID id, Attendance attendance) throws ValidationException, DatabaseAccessException {
        validateAttendance(attendance);
        attendance.setId(id);

        if (attendanceRepository.findById(id).isEmpty()) {
            throw new ValidationException("Відвідування з ID " + id + " не існує.");
        }

        persistenceContext.registerUpdated(id, attendance);
        persistenceContext.commit();
        return attendance;
    }

    @Override
    public void delete(UUID id) throws DatabaseAccessException, ValidationException {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(id);
        if (attendanceOpt.isPresent()) {
            persistenceContext.registerDeleted(attendanceOpt.get());
            persistenceContext.commit();
        }
    }

    @Override
    public Optional<Attendance> findById(UUID id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> findAll(int offset, int limit) {
        return attendanceRepository.findAll(offset, limit);
    }

    @Override
    public List<Attendance> findByEventId(UUID eventId) {
        return attendanceRepository.findByEventId(eventId);
    }


    private void validateAttendance(Attendance attendance) {
        if (attendance == null) {
            throw new ValidationException("Відвідування не може бути null.");
        }
        if (attendance.getUserId() == null || attendance.getEventId() == null) {
            throw new ValidationException("UserId та EventId не можуть бути null.");
        }
    }
}

