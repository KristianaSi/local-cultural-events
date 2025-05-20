package com.kristiana.presentation.pages;

import static com.kristiana.presentation.PrintColor.*;

import com.kristiana.domain.entities.User.Role;
import com.kristiana.presentation.Renderable;
import com.kristiana.application.contract.AuthService;
import com.kristiana.application.contract.SignUpService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.application.dto.AddDtoUser;
import com.kristiana.application.exception.AuthException;
import com.kristiana.application.exception.SignUpException;
import com.kristiana.domain.entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class AuthView implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public AuthView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void process(AuthMenu selectedItem) throws IOException {
        AuthService authService = serviceFactory.getAuthService();
        SignUpService signUpService = serviceFactory.getSignUpService();

        switch (selectedItem) {
            case SIGN_IN -> {
                printBlue("Впишіть ваш логін: ");
                String username = reader.readLine();

                printBlue("Впишіть ваш пароль: ");
                String password = reader.readLine();

                try {
                    boolean authenticate = authService.login(username, password);
                    if (authenticate) {
                        User user = authService.getCurrentUser();
                        System.out.printf("Аутентифікація успішна. Роль: %s%n", user.getRole());
                        new MenuView(user.getRole(), serviceFactory).render();
                    } else {
                        printRed("Аутентифікація неуспішна.");
                    }
                } catch (AuthException e) {
                    printRed("Помилка аутентифікації: " + e.getCause());
                }
            }
            case SIGN_UP -> {
                printBlue("Впишіть ваш логін: ");
                String username = reader.readLine();

                printBlue("Впишіть ваш пароль: ");
                String password = reader.readLine();

                printBlue("Вкажіть вашу електронну пошту: ");
                String email = reader.readLine();

                try {
                    AddDtoUser addDtoUser = new AddDtoUser(
                        username,
                        password,
                        email,
                        Role.GENERAL
                    );

                    signUpService.signUp(addDtoUser, () -> {
                        printBlue("Введіть код підтвердження з вашої пошти: ");
                        try {
                            return reader.readLine();
                        } catch (IOException e) {
                            throw new SignUpException("Помилка коду підтвердження: " + e);
                        }
                    });

                    printGreen("Реєстрація успішна!");

                    boolean authenticate = authService.login(username, password);
                    if (authenticate) {
                        User user = authService.getCurrentUser();
                        System.out.printf("Аутентифікація успішна. Роль: %s%n", user.getRole());
                        new MenuView(user.getRole(), serviceFactory).render();
                    } else {
                        printRed("Автоматична аутентифікація не вдалася.");
                    }
                } catch (SignUpException e) {
                    printRed("Помилка реєстрації: \n" + e.getCause());
                }
            }
            case EXIT -> printRed("Вихід з програми...");
            default -> printRed("Неправильний вибір");
        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurple("\n\n=== Меню ===");
            System.out.println("1. " + AuthMenu.SIGN_IN.getName());
            System.out.println("2. " + AuthMenu.SIGN_UP.getName());
            System.out.println("0. " + AuthMenu.EXIT.getName());
            System.out.print("Зробіть вибір: ");

            String choice = reader.readLine();
            AuthMenu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> AuthMenu.SIGN_IN;
                    case "2" -> AuthMenu.SIGN_UP;
                    case "0" -> AuthMenu.EXIT;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == AuthMenu.EXIT) {
                    process(AuthMenu.EXIT);
                    System.exit(0);
                }

                process(selectedItem);
            } catch (IllegalArgumentException e) {
                printRed("Помилка: " + e.getCause());
            }
        }
    }

    enum AuthMenu {
        SIGN_IN("Авторизація"),
        SIGN_UP("Реєстрація"),
        EXIT("Вихід");

        private final String name;

        AuthMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}