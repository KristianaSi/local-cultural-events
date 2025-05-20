package com.kristiana.application.impl;

import com.kristiana.application.contract.EventService;
import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.PersistenceContext;
import com.kristiana.infrastructure.persistence.contract.EventRepository;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реалізація сервісу для управління сутностями подій.
 */
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final PersistenceContext persistenceContext;

    public EventServiceImpl(EventRepository eventRepository, PersistenceContext persistenceContext) {
        this.eventRepository = eventRepository;
        this.persistenceContext = persistenceContext;
    }

    @Override
    public Event create(Event event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }

        // Валідація на рівні бізнес-логіки (наприклад, перевірка на наявність події з таким іменем)
        validateEvent(event);

        persistenceContext.registerNew(event);
        persistenceContext.commit();
        return event;
    }

    @Override
    public Event update(UUID id, Event event) {
        event.setId(id);

        if (!eventRepository.findById(id).isPresent()) {
            throw new DatabaseAccessException("Подія з ідентифікатором " + id + " не існує.");
        }

        // Валідація на рівні бізнес-логіки
        validateEvent(event);

        persistenceContext.registerUpdated(id, event);
        persistenceContext.commit();
        return event;
    }

    @Override
    public void delete(UUID id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            persistenceContext.registerDeleted(eventOpt.get());
            persistenceContext.commit();
        } else {
            throw new DatabaseAccessException("Подія з ідентифікатором " + id + " не знайдена.");
        }
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> findAll(int offset, int limit) {
        return eventRepository.findAll(offset, limit);
    }

    @Override
    public List<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    @Override
    public List<Event> findByPartialName(String partialName) {
        return eventRepository.findByPartialName(partialName);
    }

    private void validateEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Подія не може бути null.");
        }
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Назва події не може бути порожньою.");
        }
        // Додаткові умови для перевірки події
    }
}
