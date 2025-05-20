package com.kristiana.presentation.forms;

import com.kristiana.application.contract.CategoryService;
import com.kristiana.application.dto.AddDtoCategory;
import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.infrastructure.persistence.exception.DatabaseAccessException;
import com.kristiana.presentation.Renderable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CategoryAddForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final ServiceFactory serviceFactory;

    public CategoryAddForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    void process() throws IOException {
        CategoryService categoryService = serviceFactory.getCategoryService();

        String name = readNonEmptyLine("Впишіть назву категорії: ");
        String description = readNonEmptyLine("Впишіть опис категорії: ");

        try {
            AddDtoCategory category = new AddDtoCategory(
                name,
                description
            );
            categoryService.create(category);
            System.out.println("Категорія успішно створена!");
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
        System.out.println("\n~~~ Створення нової категорії ~~~");
        process();
    }
}
