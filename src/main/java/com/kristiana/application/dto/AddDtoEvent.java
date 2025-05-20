package com.kristiana.application.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AddDtoEvent(
    @NotBlank(message = "Назва події не може бути порожньою")
    @Size(max = 128, message = "Назва має бути до 128 символів")
    String name,

    String description,

    @NotNull(message = "ID локації обов'язковий")
    Integer locationId,

    @NotNull(message = "ID категорії обов'язковий")
    Integer categoryId,

    @NotNull(message = "Дата події обов'язкова")
    @FutureOrPresent(message = "Дата має бути сьогодні або в майбутньому")
    LocalDate date
) {
}
