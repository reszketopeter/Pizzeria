package tutorial.pizzeria.exception;

public class CustomerIdIsNullException extends RuntimeException {

    public CustomerIdIsNullException(String message) {
        super(message);
    }
}
