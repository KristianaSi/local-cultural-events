package com.kristiana.application.impl;

import com.kristiana.application.contract.LocationService;
import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.Location;
import com.kristiana.infrastructure.persistence.PersistenceContext;
import com.kristiana.infrastructure.persistence.contract.LocationRepository;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реалізація сервісу для управління сутностями локацій.
 */
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final PersistenceContext persistenceContext;

    public LocationServiceImpl(LocationRepository locationRepository, PersistenceContext persistenceContext) {
        this.locationRepository = locationRepository;
        this.persistenceContext = persistenceContext;
    }

    @Override
    public Location create(Location location) throws ValidationException, DatabaseAccessException {
        validateLocation(location);

        if (locationRepository.existsByName(location.getName())) {
            throw new ValidationException("Локація з назвою '" + location.getName() + "' уже існує.");
        }

        persistenceContext.registerNew(location);
        persistenceContext.commit();
        return location;
    }

    @Override
    public Location update(UUID id, Location location) throws ValidationException, DatabaseAccessException {
        validateLocation(location);
        location.setId(id);

        Optional<Location> existingLocation = locationRepository.findById(id);
        if (!existingLocation.isPresent()) {
            throw new ValidationException("Локація з ідентифікатором " + id + " не існує.");
        }

        persistenceContext.registerUpdated(id, location);
        persistenceContext.commit();
        return location;
    }

    @Override
    public void delete(UUID id) throws DatabaseAccessException, ValidationException {
        Optional<Location> locationOpt = locationRepository.findById(id);
        if (!locationOpt.isPresent()) {
            throw new ValidationException("Локація з ідентифікатором " + id + " не знайдена.");
        }

        persistenceContext.registerDeleted(locationOpt.get());
        persistenceContext.commit();
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<Location> findAll(int offset, int limit) {
        return locationRepository.findAll(offset, limit);
    }

    @Override
    public List<Location> findByPartialName(String partialName) {
        return locationRepository.findByNameContainingIgnoreCase(partialName);
    }

    private void validateLocation(Location location) throws ValidationException {
        if (location == null) {
            throw new ValidationException("Локація не може бути null.");
        }
        if (location.getName() == null || location.getName().trim().isEmpty()) {
            throw new ValidationException("Назва локації не може бути порожньою.");
        }
    }
}

