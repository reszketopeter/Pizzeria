package tutorial.pizzeria.exception;

public class CustomerNotRegisteredYetException extends RuntimeException {

    public CustomerNotRegisteredYetException(String message) {
        super(message);
    }
}
