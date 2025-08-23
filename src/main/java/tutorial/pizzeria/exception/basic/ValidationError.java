package tutorial.pizzeria.exception.basic;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError {

    private final List<CustomFieldError> errors = new ArrayList<>();

    public void addingCustomFieldError(String field, String message) {
        errors.add(new CustomFieldError(field, message));
    }

}
