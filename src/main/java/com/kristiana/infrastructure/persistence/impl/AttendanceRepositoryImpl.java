package com.kristiana.infrastructure.persistence.impl;


import com.kristiana.domain.entities.Attendance;
import com.kristiana.infrastructure.persistence.GenericRepository;
import com.kristiana.infrastructure.persistence.contract.AttendanceRepository;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AttendanceRepositoryImpl extends GenericRepository<Attendance, UUID> implements AttendanceRepository {

    public AttendanceRepositoryImpl(ConnectionPool connectionPool) {
        super(connectionPool, Attendance.class, "attendances");
    }

    @Override
    public List<Attendance> findByName(String name) {
        return findByField("name", name);
    }

    @Override
    public List<Attendance> findByEventId(UUID eventId) {
        String sql = "SELECT * FROM attendances WHERE event_id = ?";
        return executeQuery(sql, stmt -> stmt.setObject(1, eventId), resultSet -> {
            Attendance attendance = new Attendance();
            attendance.setId(UUID.fromString(resultSet.getString("id")));
            attendance.setName(resultSet.getString("name"));
            attendance.setDescription(resultSet.getString("description"));
            attendance.setStatus(resultSet.getBoolean("status"));
            attendance.setEventId(UUID.fromString(resultSet.getString("event_id")));
            attendance.setUserId(UUID.fromString(resultSet.getString("user_id")));
            return attendance;
        });
    }

    @Override
    public List<Attendance> findByUserId(UUID userId) {
        String sql = "SELECT * FROM attendances WHERE user_id = ?";
        return executeQuery(sql, stmt -> stmt.setObject(1, userId), resultSet -> {
            Attendance attendance = new Attendance();
            attendance.setId(UUID.fromString(resultSet.getString("id")));
            attendance.setName(resultSet.getString("name"));
            attendance.setDescription(resultSet.getString("description"));
            attendance.setStatus(resultSet.getBoolean("status"));
            attendance.setEventId(UUID.fromString(resultSet.getString("event_id")));
            attendance.setUserId(UUID.fromString(resultSet.getString("user_id")));
            return attendance;
        });
    }

    @Override
    public List<Attendance> findByStatus(boolean status) {
        String sql = "SELECT * FROM attendances WHERE status = ?";
        return executeQuery(sql, stmt -> stmt.setBoolean(1, status), resultSet -> {
            Attendance attendance = new Attendance();
            attendance.setId(UUID.fromString(resultSet.getString("id")));
            attendance.setName(resultSet.getString("name"));
            attendance.setDescription(resultSet.getString("description"));
            attendance.setStatus(resultSet.getBoolean("status"));
            attendance.setEventId(UUID.fromString(resultSet.getString("event_id")));
            attendance.setUserId(UUID.fromString(resultSet.getString("user_id")));
            return attendance;
        });
    }

    @Override
    public List<Attendance> findByNameContainingIgnoreCase(String partialName) {
        return findAll(
            (whereClause, params) -> {
                whereClause.add("name ILIKE ?");
                params.add("%" + partialName + "%");
            },
            null, true, 0, Integer.MAX_VALUE
        );
    }

    @Override
    public long countByEventId(UUID eventId) {
        String sql = "SELECT COUNT(*) FROM attendances WHERE event_id = ?";
        return executeCountQuery(sql, List.of(eventId));
    }

    @Override
    public boolean existsByName(String name) {
        return count((whereClause, params) -> {
            whereClause.add("name = ?");
            params.add(name);
        }) > 0;
    }
}
