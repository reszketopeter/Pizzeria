package tutorial.pizzeria.exception.basic;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.boot.json.JsonParseException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tutorial.pizzeria.exception.CategoryAlreadyExistsException;
import tutorial.pizzeria.exception.CustomerNotFoundException;
import tutorial.pizzeria.exception.EmailAlreadyExistsException;
import tutorial.pizzeria.exception.InvalidPasswordException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
/*Ez azt jelenti, hogy a GlobalExceptionHandler osztályban definiált kivételkezelők az egész alkalmazásban érvényesek
 lesznek, és minden vezérlőben (controller) előforduló kivételeket kezelni tudnak.*/
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Central method to log all exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(HttpServletRequest request, Exception ex) {
        String requestId = MDC.get("requestId");
        return new ResponseEntity<>("Internal Server Error. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                                 MethodArgumentNotValidException ex) {
        String requestId = MDC.get("requestId");

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        return new ResponseEntity<>(makeValidationError(fieldErrors), HttpStatus.BAD_REQUEST);
    }

    private ValidationError makeValidationError(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();
        for (FieldError fieldError : fieldErrors) {
            validationError.addingCustomFieldError(
                    fieldError.getField(),
                    messageSource.getMessage(fieldError, Locale.getDefault())
            );
        }
        return validationError;
    }

    // Handle JSON parsing errors
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ApiError> handleJsonParseException(HttpServletRequest request, JsonParseException ex) {
        String requestId = MDC.get("requestId");

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError body = new ApiError("JSON_PARSE_ERROR", "Invalid JSON request.",
                ex.getLocalizedMessage());
        return new ResponseEntity<>(body, status);
    }

    // Handle illegal state exceptions
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiException> handleIllegalStateException(IllegalStateException ex) {
        String requestId = MDC.get("requestId");

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException response = new ApiException(ex.getMessage(), status, ZonedDateTime.now().toString());

        return new ResponseEntity<>(response, status);
    }

    // Handle illegal argument exceptions
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        String requestId = MDC.get("requestId");

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError body = new ApiError("ILLEGAL_ARGUMENT_ERROR", "Invalid argument passed to the method.",
                ex.getLocalizedMessage());

        return new ResponseEntity<>(body, status);
    }

    // Handle unexpected errors
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> defaultErrorHandler(HttpServletRequest request, Throwable t) {
        String requestId = MDC.get("requestId");

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError body = new ApiError("UNCLASSIFIED_ERROR", "An unexpected error occurred.",
                t.getLocalizedMessage());

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiException> handleCustomerNotRegisteredYet(CustomerNotFoundException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiExceptionMake(exception, status), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiException> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiExceptionMake(exception, status), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiException> handleInvalidPassword(InvalidPasswordException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiExceptionMake(exception, status), HttpStatus.BAD_REQUEST);
    }

    private <T extends RuntimeException> ApiException apiExceptionMake(T exception, HttpStatus status) {
        return new ApiException(exception.getMessage(), status, ZonedDateTime.now().toString());
    }

    //Handle category
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiException> handleCategories(CategoryAlreadyExistsException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(apiExceptionMake(exception, status), HttpStatus.BAD_REQUEST);
    }
}
