package com.kristiana.application.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateDtoEvent(
    @NotNull(message = "ID події обов'язковий")
    UUID EventId,

    @NotBlank(message = "Назва події не може бути порожньою")
    String name,

    @NotNull(message = "ID локації обов'язковий")
    UUID locationId,

    @NotNull(message = "ID категорії обов'язковий")
    UUID categoryId,

    @NotNull(message = "Дата події обов'язкова")
    @FutureOrPresent(message = "Дата не може бути в минулому")
    LocalDate date
) {
}
