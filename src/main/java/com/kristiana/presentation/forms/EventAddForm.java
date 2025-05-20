package com.kristiana.presentation.forms;

import com.kristiana.application.contract.EventService;
import com.kristiana.application.contract.LocationService;
import com.kristiana.application.contract.CategoryService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Event;
import com.kristiana.domain.entities.Location;
import com.kristiana.domain.entities.Category;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;

import com.kristiana.presentation.Renderable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

public class EventAddForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public EventAddForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        EventService eventService = serviceFactory.getEventService();
        LocationService locationService = serviceFactory.getLocationService();
        CategoryService categoryService = serviceFactory.getCategoryService();

        String name = readNonEmptyLine("Впишіть назву події: ");
        LocalDate date = readValidDate("Вкажіть дату події у форматі yyyy-mm-dd: ");

        // Вибір категорії з доступних
        List<Category> allCategories = categoryService.findAll(0, 100);
        if (allCategories.isEmpty()) {
            System.out.println("Немає доступних категорій.");
            return;
        }
        System.out.println("\nДоступні категорії:");
        for (int i = 0; i < allCategories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, allCategories.get(i).getName());
        }
        Category selectedCategory = null;
        while (true) {
            System.out.println("Виберіть номер категорії (0 для виходу): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) return;
                if (choice > 0 && choice <= allCategories.size()) {
                    selectedCategory = allCategories.get(choice - 1);
                    break;
                }
                System.out.println("Невірний номер, спробуйте ще раз.");
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        // Вибір локації з доступних
        List<Location> allLocations = locationService.findAll(0, 100);
        if (allLocations.isEmpty()) {
            System.out.println("Немає доступних локацій.");
            return;
        }
        System.out.println("\nДоступні локації:");
        for (int i = 0; i < allLocations.size(); i++) {
            Location loc = allLocations.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, loc.getName(), loc.getDescription());
        }
        Location location = null;
        while (true) {
            System.out.println("Виберіть номер локації (0 для виходу): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) return;
                if (choice > 0 && choice <= allLocations.size()) {
                    location = allLocations.get(choice - 1);
                    break;
                }
                System.out.println("Невірний номер, спробуйте ще раз.");
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        String description = readNonEmptyLine("Впишіть опис події: ");

        try {
            Event event = new Event(
                UUID.randomUUID(),
                name,
                description,
                date,
                location.getId(),
                selectedCategory.getId()   // передаємо ім'я обраної категорії
            );
            eventService.create(event);
            System.out.println("Подія успішно створена!");
        } catch (DatabaseAccessException | IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    private String readNonEmptyLine(String prompt) throws IOException {
        String input;
        do {
            System.out.println(prompt);
            input = reader.readLine().trim();
            if (input.isEmpty()) {
                System.out.println("Поле не може бути порожнім.");
            }
        } while (input.isEmpty());
        return input;
    }

    private LocalDate readValidDate(String prompt) throws IOException {
        LocalDate date = null;
        while (date == null) {
            System.out.println(prompt);
            try {
                date = LocalDate.parse(reader.readLine().trim());
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Дата не може бути в минулому.");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Неправильний формат дати.");
            }
        }
        return date;
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Створення нової події ~~~");
        process();
    }
}
