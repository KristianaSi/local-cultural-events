package com.kristiana.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddDtoCategory(
    @NotBlank(message = "Назва категорії не може бути порожньою")
    @Size(max = 128, message = "Назва має бути до 128 символів")
    String name,
    String description
) {
}

