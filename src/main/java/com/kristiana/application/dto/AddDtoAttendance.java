package com.kristiana.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public record AddDtoAttendance(
    @NotNull(message = "ID події обов'язковий")
    UUID eventId,
    String name,
    String description,

    @NotNull(message = "ID користувача обов'язковий")
    UUID userId,

    @NotBlank(message = "Статус не може бути порожнім")
    @Pattern(regexp = "^(APPROVED|REJECTED|PENDING)$", message = "Недопустиме значення статусу")
    boolean status
) {
}
