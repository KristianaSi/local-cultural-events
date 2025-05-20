package com.kristiana.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddDtoLocation(
    @NotBlank(message = "Назва не може бути порожньою")
    @Size(max = 128, message = "Назва має бути до 128 символів")
    String name,

    String description, //опис

    @NotBlank(message = "Адреса не може бути порожньою")
    @Size(max = 512, message = "Адреса має бути до 512 символів")
    String address
) {
}
