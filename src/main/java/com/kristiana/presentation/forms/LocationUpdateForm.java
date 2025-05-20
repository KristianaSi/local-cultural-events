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

public class LocationUpdateForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public LocationUpdateForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        LocationService locationService = serviceFactory.getLocationService();

        List<Location> allLocations = locationService.findAll(0, 100);
        if (allLocations.isEmpty()) {
            System.out.println("Немає доступних локацій для оновлення.");
            return;
        }

        System.out.println("\nДоступні локації:");
        for (int i = 0; i < allLocations.size(); i++) {
            Location loc = allLocations.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, loc.getName(), loc.getDescription());
        }

        Location selectedLocation = null;
        while (true) {
            System.out.println("Виберіть номер локації для оновлення (0 для виходу): ");
            String input = reader.readLine().trim();
            if ("0".equals(input)) {
                return;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice > 0 && choice <= allLocations.size()) {
                    selectedLocation = allLocations.get(choice - 1);
                    break;
                } else {
                    System.out.println("Невірний номер, спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        String newName = readNonEmptyLine("Введіть нову назву локації (поточна: " + selectedLocation.getName() + "): ");
        String newDescription = readNonEmptyLine("Введіть новий опис локації (поточний: " + selectedLocation.getDescription() + "): ");

        Location updatedLocation = new Location(
            selectedLocation.getId(),
            newName,
            newDescription,
            selectedLocation.getAddress() // потрібне четверте поле (наприклад, адреса або інший атрибут)
        );


        try {
            locationService.update(updatedLocation.getId(), updatedLocation);
            System.out.println("Локація успішно оновлена.");
        } catch (DatabaseAccessException | IllegalArgumentException e) {
            System.err.println("Помилка при оновленні локації: " + e.getMessage());
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
        System.out.println("\n~~~ Оновлення локації ~~~");
        process();
    }
}
