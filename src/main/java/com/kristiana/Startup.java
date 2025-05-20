package com.kristiana;

import static com.kristiana.presentation.PrintColor.printPurple;

import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.application.contract.AuthService;
import com.kristiana.domain.entities.User;
import com.kristiana.presentation.PageFactory;

import java.io.IOException;

public class Startup {

    private final AuthService authService;
    private final ServiceFactory serviceFactory;
    private final PageFactory pageFactory;

    public Startup(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.authService = serviceFactory.getAuthService();
        this.pageFactory = PageFactory.getInstance(serviceFactory);
    }

    public void start() {
        try {
            printPurple("=== Ласкаво просимо до системи Культурних Подій ===");

            if (authService.isAuthenticated()) {
                User.Role role = authService.getCurrentUser().getRole();
                pageFactory.createMenuView(role).render();
            } else {
                pageFactory.createAuthView().render();
            }

        } catch (IOException e) {
            throw new RuntimeException("Сталася помилка під час запуску застосунку", e);
        }
    }
}
