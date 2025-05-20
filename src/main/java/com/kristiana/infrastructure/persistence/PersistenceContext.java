package com.kristiana.infrastructure.persistence;

import com.kristiana.domain.entities.*;
import com.kristiana.infrastructure.persistence.contract.*;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class PersistenceContext {

    private final ConnectionPool connectionPool;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private Connection connection;
    private final Map<Class<?>, Repository<?, ?>> repositories;
    private final List<Object> newEntities;
    private final Map<Object, Object> updatedEntities;
    private final List<Object> deletedEntities;

    public PersistenceContext(ConnectionPool connectionPool,
        EventRepository eventRepository,
        CategoryRepository categoryRepository,
        LocationRepository locationRepository,
        AttendanceRepository attendanceRepository,
        UserRepository userRepository) {
        this.connectionPool = connectionPool;
        this.eventRepository = eventRepository;
        this.attendanceRepository = attendanceRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;

        this.repositories = new HashMap<>();
        this.newEntities = new ArrayList<>();
        this.updatedEntities = new HashMap<>();
        this.deletedEntities = new ArrayList<>();

        // Реєстрація репозиторіїв
        this.registerRepository(Event.class, eventRepository);
        this.registerRepository(Category.class, categoryRepository);
        this.registerRepository(Attendance.class, attendanceRepository);
        this.registerRepository(Location.class, locationRepository);
        this.registerRepository(User.class, userRepository);

        initializeConnection();
    }

    public <T, ID> void registerRepository(Class<T> entityClass, Repository<T, ID> repository) {
        repositories.put(entityClass, repository);
    }

    public void registerNew(Object entity) {
        if (entity == null) throw new IllegalArgumentException("Сутність не може бути null");
        newEntities.add(entity);
    }

    public void registerUpdated(Object id, Object entity) {
        if (id == null || entity == null)
            throw new IllegalArgumentException("Ідентифікатор або сутність не можуть бути null");
        updatedEntities.put(id, entity);
    }

    public void registerDeleted(Object entity) {
        if (entity == null) throw new IllegalArgumentException("Сутність не може бути null");
        deletedEntities.add(entity);
    }

    public void commit() {
        try {
            for (Object entity : newEntities) {
                Repository<Object, Object> repository = getRepository(entity.getClass());
                repository.save(entity);
            }

            for (Map.Entry<Object, Object> entry : updatedEntities.entrySet()) {
                Repository<Object, Object> repository = getRepository(entry.getValue().getClass());
                repository.update(entry.getKey(), entry.getValue());
            }

            for (Object entity : deletedEntities) {
                Repository<Object, Object> repository = getRepository(entity.getClass());
                Object id = repository.extractId(entity);
                repository.delete(id);
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseAccessException("Помилка відкатування транзакції", rollbackEx);
            }
            throw new DatabaseAccessException("Помилка виконання транзакції", e);
        } finally {
            clear();
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseAccessException("Помилка закриття з'єднання", e);
            }
        }
    }

    private void clear() {
        newEntities.clear();
        updatedEntities.clear();
        deletedEntities.clear();
    }

    private void initializeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            this.connection = connectionPool.getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseAccessException("Помилка ініціалізації з'єднання", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T, ID> Repository<T, ID> getRepository(Class<?> entityClass) {
        Repository<T, ID> repository = (Repository<T, ID>) repositories.get(entityClass);
        if (repository == null) {
            throw new IllegalStateException("Репозиторій для " + entityClass.getSimpleName() + " не зареєстровано");
        }
        return repository;
    }
}
