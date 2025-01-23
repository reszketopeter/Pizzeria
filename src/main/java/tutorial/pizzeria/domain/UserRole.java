package tutorial.pizzeria.domain;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("Admin"),
    GUEST("Guest");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
