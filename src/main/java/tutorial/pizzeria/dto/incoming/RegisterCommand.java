package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterCommand {

    @NotBlank(message = "Name must be not empty!")
    private String name;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "The password must contain at least one lowercase letter, one uppercase letter," +
                    " one number and must be at least 8 characters long!"
    )
    @NotBlank(message = "Password must be not empty!")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must be not empty")
    private String email;

    @NotBlank(message = "Phone must be not empty")
    @Pattern(
            regexp = "^\\+\\d{0,15}$",
            message = "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                    "Don't use spaces, hyphen or dashes!"
    )
    private Integer phone;

    @NotBlank(message = "Postal code must be not empty")
    private Integer postalCode;

    @NotBlank(message = "City must be not empty")
    private String city;

    @NotBlank(message = "Address must be not empty")
    private String address;
}
