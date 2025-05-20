package com.kristiana;

import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.infrastructure.InfrastructureConfig;
import com.kristiana.infrastructure.persistence.PersistenceContext;
import com.kristiana.infrastructure.persistence.util.ConnectionPool;
import com.kristiana.infrastructure.persistence.util.PersistenceInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class Application {

    private final PersistenceContext persistenceContext;
    private final PersistenceInitializer persistenceInitializer;
    private final ConnectionPool connectionPool;
    private final ServiceFactory serviceFactory;

    public Application(PersistenceContext persistenceContext,
        PersistenceInitializer persistenceInitializer,
        ConnectionPool connectionPool,
        ServiceFactory serviceFactory) {
        this.persistenceContext = persistenceContext;
        this.persistenceInitializer = persistenceInitializer;
        this.connectionPool = connectionPool;
        this.serviceFactory = serviceFactory;
    }

    public void run() {
        // Очистити та ініціалізувати базу даних
        persistenceInitializer.clearData();
        persistenceInitializer.init();

        // Запуск головного меню
        Startup startup = new Startup(serviceFactory);
        startup.start();

        // Завершення роботи з пулом з’єднань
        connectionPool.shutdown();
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(InfrastructureConfig.class, AppConfig.class);
        Application app = context.getBean(Application.class);
        app.run();
    }

    @Configuration
    static class AppConfig {
        @Bean
        public Application application(PersistenceContext persistenceContext,
            PersistenceInitializer persistenceInitializer,
            ConnectionPool connectionPool,
            ServiceFactory serviceFactory) {
            return new Application(persistenceContext, persistenceInitializer, connectionPool, serviceFactory);
        }
    }
}
