package com.kristiana.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UpdateDtoLocation(
    @NotNull(message = "ID локації обов'язковий")
    UUID id,

    @NotBlank(message = "Назва не може бути порожньою")
    String name,

    @Size(max = 512, message = "Адреса має містити не більше 512 символів")
    String address
) {
}
