package com.kristiana.presentation.pages;


import static com.kristiana.presentation.PrintColor.*;

import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.User.Role;
import com.kristiana.presentation.PageFactory;
import com.kristiana.presentation.Renderable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class MenuView implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final Role userRole;
    private final PageFactory pageFactory;
    private final ServiceFactory serviceFactory;

    public MenuView(Role userRole, ServiceFactory serviceFactory) {
        this.userRole = userRole;
        this.pageFactory = PageFactory.getInstance(serviceFactory);
        this.serviceFactory = serviceFactory;
    }

    private void process(Menu selectedItem) throws IOException {
        switch (selectedItem) {
            case VIEW_EVENTS -> pageFactory.createEventView().render();
            case ADD_EVENT -> pageFactory.createEventAddForm().render();
            case EDIT_EVENT -> pageFactory.createEventUpdateForm().render();
            case DELETE_EVENT -> pageFactory.createDeleteEventForm().render();

            case VIEW_CATEGORIES -> pageFactory.createCategoryView().render();
            case ADD_CATEGORY -> pageFactory.createCategoryAddForm().render();
            case EDIT_CATEGORY -> pageFactory.createCategoryUpdateForm().render();
            case DELETE_CATEGORY -> pageFactory.createCategoryDeleteForm().render();
            

            case LOG_OUT -> {
                printYellow("Вихід з акаунту...");
                serviceFactory.getAuthService().logout();
                pageFactory.createAuthView().render();
            }

            case EXIT -> printRed("Вихід з програми...");
            default -> printRed("Неправильний вибір");
        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurple("\n\n=== Головне меню ===");
            System.out.println("1. " + Menu.VIEW_EVENTS.getName());
            System.out.println("2. " + Menu.ADD_EVENT.getName());
            System.out.println("3. " + Menu.EDIT_EVENT.getName());
            System.out.println("4. " + Menu.DELETE_EVENT.getName());
            System.out.println("5. " + Menu.VIEW_CATEGORIES.getName());
            System.out.println("6. " + Menu.ADD_CATEGORY.getName());
            System.out.println("7. " + Menu.EDIT_CATEGORY.getName());
            System.out.println("8. " + Menu.DELETE_CATEGORY.getName());

            if (userRole == Role.ADMIN) {
                System.out.println("9. " + Menu.VIEW_ATTENDANCE.getName());
            }

            System.out.println("10. " + Menu.LOG_OUT.getName());
            System.out.println("0. " + Menu.EXIT.getName());
            printBlue("Зробіть вибір: ");

            String choice = reader.readLine();
            Menu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> Menu.VIEW_EVENTS;
                    case "2" -> Menu.ADD_EVENT;
                    case "3" -> Menu.EDIT_EVENT;
                    case "4" -> Menu.DELETE_EVENT;
                    case "5" -> Menu.VIEW_CATEGORIES;
                    case "6" -> Menu.ADD_CATEGORY;
                    case "7" -> Menu.EDIT_CATEGORY;
                    case "8" -> Menu.DELETE_CATEGORY;
                    case "9" -> Menu.VIEW_ATTENDANCE;
                    case "10" -> Menu.LOG_OUT;
                    case "0" -> Menu.EXIT;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == Menu.EXIT) {
                    process(Menu.EXIT);
                    System.exit(0);
                    break;
                }

                process(selectedItem);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getCause());
            }
        }
    }

    enum Menu {
        VIEW_EVENTS("Перегляд подій"),
        ADD_EVENT("Створити подію"),
        EDIT_EVENT("Редагувати подію"),
        DELETE_EVENT("Видалити подію"),

        VIEW_CATEGORIES("Перегляд категорій"),
        ADD_CATEGORY("Створити категорію"),
        EDIT_CATEGORY("Редагувати категорію"),
        DELETE_CATEGORY("Видалити категорію"),

        VIEW_ATTENDANCE("Перегляд відвідуваності"),

        LOG_OUT("Вийти з акаунту"),
        EXIT("Вихід");

        private final String name;

        Menu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

