package com.kristiana.presentation.forms;

import com.kristiana.application.contract.LocationService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Location;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

public class LocationDeleteForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public LocationDeleteForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        LocationService locationService = serviceFactory.getLocationService();

        List<Location> allLocations = locationService.findAll(0, 100);
        if (allLocations.isEmpty()) {
            System.out.println("Немає доступних локацій для видалення.");
            return;
        }

        System.out.println("\nДоступні локації:");
        for (int i = 0; i < allLocations.size(); i++) {
            Location loc = allLocations.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, loc.getName(), loc.getDescription());
        }

        UUID selectedId = null;
        while (true) {
            System.out.println("Виберіть номер локації для видалення (0 для виходу): ");
            String input = reader.readLine().trim();
            if ("0".equals(input)) {
                return;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice > 0 && choice <= allLocations.size()) {
                    selectedId = allLocations.get(choice - 1).getId();
                    break;
                } else {
                    System.out.println("Невірний номер, спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        try {
            locationService.delete(selectedId);
            System.out.println("Локація успішно видалена.");
        } catch (DatabaseAccessException e) {
            System.err.println("Помилка при видаленні локації: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Видалення локації ~~~");
        process();
    }
}

