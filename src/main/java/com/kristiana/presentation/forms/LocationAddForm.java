package com.kristiana.presentation.forms;

import com.kristiana.application.contract.LocationService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Location;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class LocationAddForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public LocationAddForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        LocationService locationService = serviceFactory.getLocationService();

        String name = readNonEmptyLine("Впишіть назву локації: ");
        String description = readNonEmptyLine("Впишіть опис локації: ");
        String address = readNonEmptyLine("Впишіть адресу локації: ");

        try {
            Location location = new Location(
                UUID.randomUUID(),
                name,
                description,
                address
            );
            locationService.create(location);
            System.out.println("Локація успішно створена!");
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

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Створення нової локації ~~~");
        process();
    }
}

