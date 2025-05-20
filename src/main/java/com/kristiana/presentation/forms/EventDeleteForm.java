package com.kristiana.presentation.forms;

import com.kristiana.application.contract.EventService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EventDeleteForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public EventDeleteForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        EventService eventService = serviceFactory.getEventService();

        List<Event> allEvents = eventService.findAll(0, 100);
        if (allEvents.isEmpty()) {
            System.out.println("Немає доступних подій для видалення.");
            return;
        }

        System.out.println("\nДоступні події:");
        for (int i = 0; i < allEvents.size(); i++) {
            Event event = allEvents.get(i);
            System.out.printf("%d. %s (Дата: %s)%n", i + 1, event.getName(), event.getDate());
        }

        while (true) {
            System.out.println("Виберіть номер події для видалення (0 для виходу): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) return;
                if (choice > 0 && choice <= allEvents.size()) {
                    Event selectedEvent = allEvents.get(choice - 1);
                    eventService.delete(selectedEvent.getId());
                    System.out.println("Подія успішно видалена.");
                    return;
                }
                System.out.println("Невірний номер, спробуйте ще раз.");
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            } catch (DatabaseAccessException e) {
                System.err.println("Помилка при видаленні події: " + e.getMessage());
                return;
            }
        }
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Видалення події ~~~");
        process();
    }
}
