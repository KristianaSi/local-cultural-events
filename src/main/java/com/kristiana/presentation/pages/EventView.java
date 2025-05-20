package com.kristiana.presentation.pages;

import com.kristiana.application.contract.EventService;
import com.kristiana.domain.entities.Event;
import com.kristiana.presentation.Renderable;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.presentation.forms.EventAddForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import static com.kristiana.presentation.PrintColor.*;

public final class EventView implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final EventService eventService;
    private final ServiceFactory serviceFactory;
    private static final int PAGE_SIZE = 5;

    public EventView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.eventService = serviceFactory.getEventService();
    }

    private void process(EventMenu selectedItem) throws IOException {
        switch (selectedItem) {
            case VIEW_ALL -> {
                int offset = 0;
                while (true) {
                    List<Event> events = eventService.findAll(offset, PAGE_SIZE);
                    if (events.isEmpty()) {
                        if (offset == 0) printYellow("Список подій порожній.");
                        else printYellow("Більше подій немає.");
                        break;
                    }
                    printPurple(String.format("\nПодії (з %d по %d):", offset + 1, offset + events.size()));
                    events.forEach(event -> System.out.printf("- %s (ID: %s)%n", event.getName(), event.getId()));

                    printBlue("Натисніть Enter для наступної сторінки або введіть 'q' для виходу: ");
                    String input = reader.readLine();
                    if ("q".equalsIgnoreCase(input)) break;
                    offset += PAGE_SIZE;
                }
            }

            case ADD -> {
                new EventAddForm(serviceFactory).render();
            }

            case DELETE -> {
                printBlue("Введіть ID події для видалення: ");
                String id = reader.readLine();
                eventService.delete(UUID.fromString(id));
                printGreen("Подію успішно видалено.");
            }

            case BACK -> printYellow("Повернення до попереднього меню...");
            default -> printRed("Неправильний вибір");
        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurple("\n=== Події ===");
            System.out.println("1. " + EventMenu.VIEW_ALL.getName());
            System.out.println("2. " + EventMenu.ADD.getName());
            System.out.println("3. " + EventMenu.DELETE.getName());
            System.out.println("0. " + EventMenu.BACK.getName());
            printBlue("Зробіть вибір: ");

            String choice = reader.readLine();
            EventMenu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> EventMenu.VIEW_ALL;
                    case "2" -> EventMenu.ADD;
                    case "3" -> EventMenu.DELETE;
                    case "0" -> EventMenu.BACK;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == EventMenu.BACK) {
                    process(EventMenu.BACK);
                    break;
                }

                process(selectedItem);
            } catch (IllegalArgumentException e) {
                printRed("Помилка: " + e.getMessage());
            }
        }
    }

    enum EventMenu {
        VIEW_ALL("Переглянути всі події"),
        ADD("Створити подію"),
        DELETE("Видалити подію"),
        BACK("Назад");

        private final String name;

        EventMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
