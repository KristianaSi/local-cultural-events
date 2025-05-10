package com.kristiana.domain.entities;

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

public class Attendance {

    private UUID id;
    private String name;
    private String description;
    private boolean status;
    private UUID eventId;
    private UUID userId;


}
