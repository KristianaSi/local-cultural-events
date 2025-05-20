package com.kristiana.application.dto;

import com.kristiana.domain.entities.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record AddDtoUser(
    @NotBlank(message = "Username не може бути порожнім")
    @Size(min = 6, max = 34, message = "Довжина має бути від 6 до 34 символів")
    String username,

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 8, max = 72, message = "Довжина пароля має бути від 8 до 72 символів")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,72}$",
        message = "Пароль має містити великі і малі літери, цифру та спецсимвол"
    )
    String password,



    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Невірний формат email")
    @Size(max = 128, message = "Email має бути не довше 128 символів")
    String email,

    @NotNull(message = "Роль обов'язкова")
   Role role
) {
}
