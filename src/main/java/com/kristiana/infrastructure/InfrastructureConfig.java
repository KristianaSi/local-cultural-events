package com.kristiana.infrastructure;

import com.kristiana.application.contract.*;
import com.kristiana.application.impl.*;
import com.kristiana.infrastructure.file.FileStorageService;
import com.kristiana.infrastructure.file.impl.FileStorageServiceImpl;
import com.kristiana.infrastructure.persistence.contract.*;
import com.kristiana.infrastructure.persistence.impl.*;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import com.kristiana.infrastructure.persistence.util.ConnectionPool.PoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.kristiana.infrastructure")
@PropertySource("classpath:application.properties")
public class InfrastructureConfig {

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.pool.size}")
    private int dbPoolSize;

    @Value("${db.auto.commit}")
    private boolean dbAutoCommit;

    @Value("${file.storage.root}")
    private String storageRootPath;

    @Value("${file.storage.allowed-extensions}")
    private String[] allowedExtensions;

    @Value("${file.storage.max-size}")
    private long maxFileSize;

    @Bean
    public ConnectionPool connectionPool() {
        PoolConfig poolConfig = new PoolConfig.Builder()
            .withUrl(dbUrl)
            .withUser(dbUsername)
            .withPassword(dbPassword)
            .withMaxConnections(dbPoolSize)
            .withAutoCommit(dbAutoCommit)
            .build();
        return new ConnectionPool(poolConfig);
    }

    @Bean
    public FileStorageService fileStorageService() {
        return new FileStorageServiceImpl(storageRootPath, allowedExtensions, maxFileSize);
    }

    @Bean
    public PasswordService passwordService() {
        return new PasswordServiceImpl();
    }

    @Bean
    public ServiceFactory serviceFactory(ConnectionPool connectionPool,
        EventRepository eventRepository,
        CategoryRepository categoryRepository,
        LocationRepository locationRepository,
        AttendanceRepository attendanceRepository,
        UserRepository userRepository,
        PasswordService passwordService) {
        return new ServiceFactory(connectionPool,
            eventRepository,
            categoryRepository,
            locationRepository,
            attendanceRepository,
            userRepository,
            passwordService);
    }
}