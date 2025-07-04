package com.kristiana.domain.entities;

import java.util.Objects;
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
public class User implements Comparable<User> {

    private UUID id;
    private String username;
    private String passwordHash;
    private String email;
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(User other) {
        // Compare by username
        return this.username.compareToIgnoreCase(other.username);
    }

    public enum Role {
        BANNED("banned"),
        ADMIN("admin"),
        GENERAL("general");

        String name;

        Role(String name) {

            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


}
