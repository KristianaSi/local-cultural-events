package com.kristiana.presentation.forms;

import com.kristiana.application.contract.AttendanceService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class AttendanceDeleteForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public AttendanceDeleteForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void process() throws IOException {
        AttendanceService attendanceService = serviceFactory.getAttendanceService();

        System.out.print("Введіть ID відвідуваності для видалення: ");
        String idStr = reader.readLine().trim();

        try {
            UUID id = UUID.fromString(idStr);
            attendanceService.delete(id);
            System.out.println("Відвідуваність успішно видалена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Невірний формат UUID.");
        } catch (DatabaseAccessException e) {
            System.out.println("Помилка доступу до бази даних: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Видалення відвідуваності ~~~");
        process();
    }
}
