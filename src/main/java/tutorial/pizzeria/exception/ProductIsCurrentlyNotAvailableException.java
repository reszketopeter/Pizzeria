package tutorial.pizzeria.exception;

public class ProductIsCurrentlyNotAvailableException extends RuntimeException {

    public ProductIsCurrentlyNotAvailableException(String message) {
        super(message);
    }
}
