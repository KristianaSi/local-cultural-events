package com.kristiana.application.impl;

import com.kristiana.application.contract.*;
import com.kristiana.infrastructure.persistence.PersistenceContext;
import com.kristiana.infrastructure.persistence.contract.*;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;

public class ServiceFactory {

    private final PersistenceContext persistenceContext;

    private final AuthService authService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final EventService eventService;
    private final UserService userService;
    private final SignUpService signUpService;
    private final AttendanceService attendanceService;

    public ServiceFactory(ConnectionPool connectionPool,
        EventRepository eventRepository,
        CategoryRepository categoryRepository,
        LocationRepository locationRepository,
        AttendanceRepository attendanceRepository,
        UserRepository userRepository,
        PasswordService passwordService) {

        this.persistenceContext = new PersistenceContext(
            connectionPool,
            eventRepository,
            categoryRepository,
            locationRepository,
            attendanceRepository,
            userRepository
        );

        this.authService = new AuthServiceImpl(userRepository, passwordService);
        this.categoryService = new CategoryServiceImpl(categoryRepository, persistenceContext);
        this.locationService = new LocationServiceImpl(locationRepository, persistenceContext);
        this.eventService = new EventServiceImpl(eventRepository, persistenceContext);
        this.userService = new UserServiceImpl(userRepository, passwordService);
        this.signUpService = new SignUpServiceImpl(userService, "your-email@example.com");
        this.attendanceService = new AttendanceServiceImpl(attendanceRepository, persistenceContext);
    }

    public PersistenceContext getPersistenceContext() {
        return persistenceContext;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public UserService getUserService() {
        return userService;
    }

    public SignUpService getSignUpService() {
        return signUpService;
    }

    public AttendanceService getAttendanceService() {
        return attendanceService;
    }
}
