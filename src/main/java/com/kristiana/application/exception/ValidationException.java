package com.kristiana.application.exception;

import jakarta.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Set;

public class ValidationException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = violations;
    }

    // Додатковий конструктор:
    public ValidationException(String message) {
        super(message);
        this.violations = Collections.emptySet(); // або null — залежить від логіки
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }
    public static ValidationException create(String message, Set<? extends ConstraintViolation<?>> violations) {
        return new ValidationException(message, violations);
    }
}
