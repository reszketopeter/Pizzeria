package tutorial.pizzeria.exception.basic;

import lombok.Getter;

@Getter
public class CustomFieldError {

    private final String field;

    private final String message;

    public CustomFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
