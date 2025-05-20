package com.kristiana.presentation.forms;

import com.kristiana.application.contract.CategoryService;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.Category;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

public class CategoryDeleteForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public CategoryDeleteForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        CategoryService categoryService = serviceFactory.getCategoryService();

        List<Category> categories = categoryService.findAll(0, 100);
        if (categories.isEmpty()) {
            System.out.println("Немає доступних категорій для видалення.");
            return;
        }

        System.out.println("\nДоступні категорії:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categories.get(i).getName());
        }

        int choice = -1;
        while (choice < 0) {
            System.out.print("Виберіть номер категорії для видалення (0 для виходу): ");
            try {
                choice = Integer.parseInt(reader.readLine().trim());
                if (choice == 0) return;
                if (choice < 1 || choice > categories.size()) {
                    System.out.println("Невірний номер, спробуйте ще раз.");
                    choice = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введіть коректне число.");
            }
        }

        UUID idToDelete = categories.get(choice - 1).getId();

        try {
            categoryService.delete(idToDelete);
            System.out.println("Категорія успішно видалена.");
        } catch (DatabaseAccessException e) {
            System.err.println("Помилка при видаленні: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        System.out.println("\n~~~ Видалення категорії ~~~");
        process();
    }
}
