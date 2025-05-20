package com.kristiana.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateDtoAttendance(
    @NotNull(message = "ID події обов'язковий")
    UUID eventId,

    @NotNull(message = "ID користувача обов'язковий")
    UUID userId,

    @NotBlank(message = "Статус не може бути порожнім")
    String status
) {
}

