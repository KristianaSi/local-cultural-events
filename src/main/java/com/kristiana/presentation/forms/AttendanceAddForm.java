package com.kristiana.presentation.forms;

import com.kristiana.application.contract.AttendanceService;
import com.kristiana.application.contract.EventService;
import com.kristiana.application.exception.ValidationException;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Attendance;
import com.kristiana.domain.entities.Event;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

public class AttendanceAddForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public AttendanceAddForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void process() throws IOException {
        EventService eventService = serviceFactory.getEventService();
        AttendanceService attendanceService = serviceFactory.getAttendanceService();

        List<Event> events = eventService.findAll(0, 100);
        if (events.isEmpty()) {
            System.out.println("Немає доступних подій.");
            return;
        }

        System.out.println("\nДоступні події:");
        for (int i = 0; i < events.size(); i++) {
            System.out.printf("%d. %s (%s)%n", i + 1, events.get(i).getName(), events.get(i).getDate());
        }

        Event selectedEvent = null;
        while (selectedEvent == null) {
            System.out.print("Виберіть номер події (0 для виходу): ");
            try {
                int choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) return;
                if (choice > 0 && choice <= events.size()) {
                    selectedEvent = events.get(choice - 1);
                } else {
                    System.out.println("Невірний номер, спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        int attendanceCount = -1;
        while (attendanceCount < 0) {
            System.out.print("Введіть кількість відвідувачів: ");
            try {
                attendanceCount = Integer.parseInt(reader.readLine().trim());
                if (attendanceCount < 0) {
                    System.out.println("Кількість не може бути від’ємною.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        Attendance attendance = new Attendance(
            UUID.randomUUID(),
            "Відвідування події",           // name
            "Кількість: " + attendanceCount, // description
            true,                            // статус, наприклад true = активне
            selectedEvent.getId(),           // eventId
            UUID.randomUUID()                // тимчасово: userId, заміни на реального користувача
        );


        try {
            attendanceService.create(attendance);
            System.out.println("Відвідуваність успішно додана.");
        } catch (DatabaseAccessException | ValidationException | IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Додавання відвідуваності ~~~");
        process();
    }
}
