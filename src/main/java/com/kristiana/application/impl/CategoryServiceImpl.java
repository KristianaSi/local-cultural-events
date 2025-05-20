package com.kristiana.application.impl;

import com.kristiana.application.contract.CategoryService;
import com.kristiana.application.dto.AddDtoCategory;
import com.kristiana.application.dto.UpdateDtoCategory;
import com.kristiana.application.exception.ValidationException;
import com.kristiana.domain.entities.Event;
import com.kristiana.domain.entities.Category;
import com.kristiana.infrastructure.persistence.PersistenceContext;
import com.kristiana.infrastructure.persistence.contract.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реалізація сервісу для управління сутностями категорій, включаючи операції з пов'язаними аудіокнигами.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PersistenceContext persistenceContext;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
        PersistenceContext persistenceContext) {
        this.categoryRepository = categoryRepository;
        this.persistenceContext = persistenceContext;
    }

    @Override
    public Category create(AddDtoCategory dto) {
        UUID newId = UUID.randomUUID();

        if (categoryRepository.existsByName(dto.name())) {
            throw new ValidationException(
                "Категорія з назвою '" + dto.name() + "' уже існує.");
        }

        Category category = new Category(newId, dto.name(), dto.description());
        persistenceContext.registerNew(category);
        persistenceContext.commit();
        return category;
    }

    @Override
    public Category update(UUID id, UpdateDtoCategory dto) {

        if (!categoryRepository.findById(id).isPresent()) {
            throw new ValidationException("Категорія з ідентифікатором " + id + " не існує.");
        }

        List<Category> existingCategories = categoryRepository.findByName(dto.name());
        if (!existingCategories.isEmpty() && !existingCategories.get(0).getId().equals(id)) {
            throw new ValidationException(
                "Категорія з назвою '" + dto.name() + "' уже існує.");
        }

        Category updatedCategory = new Category(id, dto.name(), null); // Якщо є description - додайте
        persistenceContext.registerUpdated(id, updatedCategory);
        persistenceContext.commit();
        return updatedCategory;
    }


    @Override
    public void delete(UUID id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            if (countEventByCategoryId(id) > 0) {
                throw new ValidationException(
                    "Неможливо видалити категорію, оскільки вона пов'язана з аудіокнигами.");
            }

            persistenceContext.registerDeleted(categoryOpt.get());
            persistenceContext.commit();
        }
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findAll(int offset, int limit) {
        return categoryRepository.findAll(offset, limit);
    }

    @Override
    public List<Category> findByName(String name) {
        return List.of();
    }

    @Override
    public List<Category> findByPartialName(String partialName) {
        return List.of();
    }

    @Override
    public List<Event> findEventByCategoryId(UUID categoryId) {
        return List.of();
    }

    @Override
    public List<Category> findByEventId(UUID audiobookId) {
        return List.of();
    }

    @Override
    public long countEventByCategoryId(UUID categoryId) {
        return 0;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new ValidationException("Категорія не може бути null.");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Назва категорії не може бути null або порожньою.");
        }
    }
}