package tutorial.pizzeria.exception.basic;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException {

    private final String message;

    private final HttpStatus status;

    private final String timestamp;

    public ApiException(String message, HttpStatus status, String timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

}
