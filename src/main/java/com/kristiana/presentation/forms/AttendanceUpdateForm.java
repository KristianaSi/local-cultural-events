package com.kristiana.presentation.forms;

import com.kristiana.application.contract.AttendanceService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Attendance;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.UUID;

public class AttendanceUpdateForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public AttendanceUpdateForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void process() throws IOException {
        AttendanceService attendanceService = serviceFactory.getAttendanceService();

        System.out.print("Введіть ID відвідуваності для оновлення: ");
        String idStr = reader.readLine().trim();
        UUID id;

        try {
            id = UUID.fromString(idStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Невірний формат UUID.");
            return;
        }

        Optional<Attendance> existingOpt = attendanceService.findById(id);
        if (existingOpt.isEmpty()) {
            System.out.println("Відвідуваність з таким ID не знайдено.");
            return;
        }

        Attendance existing = existingOpt.get();

        System.out.print("Введіть нову назву [" + existing.getName() + "]: ");
        String name = reader.readLine().trim();
        if (!name.isEmpty()) existing.setName(name);

        System.out.print("Введіть новий опис [" + existing.getDescription() + "]: ");
        String description = reader.readLine().trim();
        if (!description.isEmpty()) existing.setDescription(description);

        System.out.print("Встановити статус активним? (true/false) [" + existing.isStatus() + "]: ");
        String statusStr = reader.readLine().trim();
        if (!statusStr.isEmpty()) existing.setStatus(Boolean.parseBoolean(statusStr));

        try {
            attendanceService.update(id, existing);
            System.out.println("Відвідуваність успішно оновлена.");
        } catch (DatabaseAccessException e) {
            System.out.println("Помилка БД: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Оновлення відвідуваності ~~~");
        process();
    }
}

