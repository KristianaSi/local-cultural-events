package com.kristiana.domain.entities;

import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сутність, що представляє жанр аудіокниги.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Comparable<Category> {

    private UUID id;
    private String name;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Category other) {
        // Compare by name
        return this.name.compareToIgnoreCase(other.name);
    }
}
