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

public class EventUpdateForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public EventUpdateForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        EventService eventService = serviceFactory.getEventService();
        LocationService locationService = serviceFactory.getLocationService();
        CategoryService categoryService = serviceFactory.getCategoryService();

        List<Event> allEvents = eventService.findAll(0, 100);
        if (allEvents.isEmpty()) {
            System.out.println("Немає доступних подій для оновлення.");
            return;
        }

        System.out.println("\nДоступні події:");
        for (int i = 0; i < allEvents.size(); i++) {
            Event ev = allEvents.get(i);
            System.out.printf("%d. %s (Дата: %s)%n", i + 1, ev.getName(), ev.getDate());
        }

        Event selectedEvent = null;
        while (true) {
            System.out.println("Виберіть номер події для оновлення (0 для виходу): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) return;
                if (choice > 0 && choice <= allEvents.size()) {
                    selectedEvent = allEvents.get(choice - 1);
                    break;
                }
                System.out.println("Невірний номер, спробуйте ще раз.");
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        System.out.printf("Поточна назва: %s%n", selectedEvent.getName());
        String name = readOptionalLine("Впишіть нову назву події або залиште порожнім для збереження поточної: ");
        if (name.isEmpty()) name = selectedEvent.getName();

        System.out.printf("Поточна дата: %s%n", selectedEvent.getDate());
        LocalDate date = readOptionalDate("Вкажіть нову дату події у форматі yyyy-mm-dd або залиште порожнім: ");
        if (date == null) date = selectedEvent.getDate();

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
            System.out.printf("Поточна категорія: %s%n", getCategoryNameById(allCategories, selectedEvent.getCategoryId()));
            System.out.println("Виберіть номер нової категорії (0 для збереження поточної): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) {
                    selectedCategory = findCategoryById(allCategories, selectedEvent.getCategoryId());
                    break;
                }
                if (choice > 0 && choice <= allCategories.size()) {
                    selectedCategory = allCategories.get(choice - 1);
                    break;
                }
                System.out.println("Невірний номер, спробуйте ще раз.");
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

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
            System.out.printf("Поточна локація: %s%n", getLocationNameById(allLocations, selectedEvent.getLocationId()));
            System.out.println("Виберіть номер нової локації (0 для збереження поточної): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) {
                    location = findLocationById(allLocations, selectedEvent.getLocationId());
                    break;
                }
                if (choice > 0 && choice <= allLocations.size()) {
                    location = allLocations.get(choice - 1);
                    break;
                }
                System.out.println("Невірний номер, спробуйте ще раз.");
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        System.out.printf("Поточний опис: %s%n", selectedEvent.getDescription());
        String description = readOptionalLine("Впишіть новий опис події або залиште порожнім для збереження поточного: ");
        if (description.isEmpty()) description = selectedEvent.getDescription();

        try {
            Event updatedEvent = new Event(
                selectedEvent.getId(),
                name,
                description,
                date,
                location.getId(),
                selectedCategory.getId()
            );
            eventService.update(selectedEvent.getId(), updatedEvent);
            System.out.println("Подія успішно оновлена!");
        } catch (DatabaseAccessException | IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    private String readOptionalLine(String prompt) throws IOException {
        System.out.println(prompt);
        return reader.readLine().trim();
    }

    private LocalDate readOptionalDate(String prompt) throws IOException {
        while (true) {
            System.out.println(prompt);
            String input = reader.readLine().trim();
            if (input.isEmpty()) return null;
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Дата не може бути в минулому.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Неправильний формат дати.");
            }
        }
    }

    private Category findCategoryById(List<Category> categories, UUID id) {
        return categories.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    private Location findLocationById(List<Location> locations, UUID id) {
        return locations.stream()
            .filter(l -> l.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    private String getCategoryNameById(List<Category> categories, UUID id) {
        Category c = findCategoryById(categories, id);
        return c != null ? c.getName() : "Невідома категорія";
    }

    private String getLocationNameById(List<Location> locations, UUID id) {
        Location l = findLocationById(locations, id);
        return l != null ? l.getName() : "Невідома локація";
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Оновлення події ~~~");
        process();
    }
}
