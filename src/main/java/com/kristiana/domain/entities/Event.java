package com.kristiana.domain.entities;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сутність, що представляє користувача системи.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Event {

    private UUID id;
    private String name;
    private String description;
    private LocalDate date;
    private UUID locationId;
    private UUID categoryId;

}