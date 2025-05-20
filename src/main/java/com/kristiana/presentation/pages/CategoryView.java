package com.kristiana.presentation.pages;

import static com.kristiana.presentation.PrintColor.*;

import com.kristiana.application.contract.CategoryService;
import com.kristiana.application.dto.AddDtoCategory;
import com.kristiana.application.dto.UpdateDtoCategory;
import com.kristiana.domain.entities.Category;
import com.kristiana.presentation.Renderable;
import com.kristiana.application.impl.ServiceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

public final class CategoryView implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final CategoryService categoryService;
    private static final int PAGE_SIZE = 5;

    public CategoryView(ServiceFactory serviceFactory) {
        this.categoryService = serviceFactory.getCategoryService();
    }

    private void process(CategoryMenu selectedItem) throws IOException {
        switch (selectedItem) {
            case VIEW_ALL -> {
                int offset = 0;
                while (true) {
                    List<Category> categories = categoryService.findAll(offset, PAGE_SIZE);
                    if (categories.isEmpty()) {
                        if (offset == 0) printYellow("Список категорій порожній.");
                        else printYellow("Більше категорій немає.");
                        break;
                    }
                    printPurple(String.format("\nКатегорії (з %d по %d):", offset + 1, offset + categories.size()));
                    categories.forEach(category -> System.out.printf("- %s (ID: %s)%n", category.getName(), category.getId()));

                    printBlue("Натисніть Enter для наступної сторінки або введіть 'q' для виходу: ");
                    String input = reader.readLine();
                    if ("q".equalsIgnoreCase(input)) break;
                    offset += PAGE_SIZE;
                }
            }

            case ADD -> {
                printBlue("Введіть назву нової категорії: ");
                String name = reader.readLine();
                printBlue("Введіть опис катерогії: ");
                String description = reader.readLine();
                categoryService.create(new AddDtoCategory(name,description));
                printGreen("Категорію успішно створено.");
            }

            case EDIT -> {
                printBlue("Введіть ID категорії для редагування: ");
                String id = reader.readLine();
                printBlue("Введіть нову назву категорії: ");
                String newName = reader.readLine();
                categoryService.update(UUID.fromString(id), new UpdateDtoCategory(UUID.fromString(id),newName));
                printGreen("Категорію успішно оновлено.");
            }

            case DELETE -> {
                printBlue("Введіть ID категорії для видалення: ");
                String id = reader.readLine();
                categoryService.delete(UUID.fromString(id));
                printGreen("Категорію успішно видалено.");
            }

            case BACK -> printYellow("Повернення до попереднього меню...");
            default -> printRed("Неправильний вибір");
        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurple("\n=== Категорії подій ===");
            System.out.println("1. " + CategoryMenu.VIEW_ALL.getName());
            System.out.println("2. " + CategoryMenu.ADD.getName());
            System.out.println("3. " + CategoryMenu.EDIT.getName());
            System.out.println("4. " + CategoryMenu.DELETE.getName());
            System.out.println("0. " + CategoryMenu.BACK.getName());
            printBlue("Зробіть вибір: ");

            String choice = reader.readLine();
            CategoryMenu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> CategoryMenu.VIEW_ALL;
                    case "2" -> CategoryMenu.ADD;
                    case "3" -> CategoryMenu.EDIT;
                    case "4" -> CategoryMenu.DELETE;
                    case "0" -> CategoryMenu.BACK;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == CategoryMenu.BACK) {
                    process(CategoryMenu.BACK);
                    break;
                }

                process(selectedItem);
            } catch (IllegalArgumentException e) {
                printRed("Помилка: " + e.getMessage());
            }
        }
    }

    enum CategoryMenu {
        VIEW_ALL("Переглянути всі категорії"),
        ADD("Створити категорію"),
        EDIT("Редагувати категорію"),
        DELETE("Видалити категорію"),
        BACK("Назад");

        private final String name;

        CategoryMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
