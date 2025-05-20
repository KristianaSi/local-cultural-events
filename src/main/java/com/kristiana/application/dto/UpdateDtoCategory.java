package com.kristiana.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateDtoCategory(
    @NotNull(message = "ID категорії обов'язковий")
    UUID CategoryId,

    @NotBlank(message = "Назва не може бути порожньою")
    String name
) {
}
