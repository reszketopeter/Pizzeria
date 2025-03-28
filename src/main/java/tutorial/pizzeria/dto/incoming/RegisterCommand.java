package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCommand {

    @NotBlank(message = "Name must be not empty!")
    private String name;

    @NotBlank(message = "Password must be not empty!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "The password must contain at least one lowercase letter, one uppercase letter," +
                    " one number and must be at least 8 characters long!"
    )
    private String password;

    @NotBlank(message = "Email must be not empty!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Phone must be not empty!")
    @Pattern(
            regexp = "^\\+\\d{0,15}$",
            message = "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                    "Don't use spaces, hyphen or dashes!"
    )
    private String phone;

    @NotNull(message = "Postal code must be not null!")
    private Integer postalCode;

    @NotBlank(message = "City must be not empty!")
    private String city;

    @NotBlank(message = "Address must be not empty!")
    private String address;
}
