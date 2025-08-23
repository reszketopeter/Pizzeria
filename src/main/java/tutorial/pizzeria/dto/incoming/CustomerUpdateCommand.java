package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerUpdateCommand {

    @NotBlank(message = "Email must be not empty!")
    @Email(message = "Invalid email format!")
    @Schema(description = "Customer's email address", example = "test.peter@gmail.com")
    private String email;

    @NotBlank(message = "Phone must be not empty!")
    @Pattern(
            regexp = "^\\+\\d{0,15}$",
            message = "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                    "Don't use spaces, hyphen or dashes!"
    )
    @Schema(description = "Customer's phone number", example = "+362586987")
    private String phone;

    @NotNull(message = "Postal code must be not null!")
    @Schema(description = "Customer's postal code", example = "1039 or 21547")
    private Integer postalCode;

    @NotBlank(message = "City must be not empty!")
    @Schema(description = "The name of the city/village where the customer lives", example = "London")
    private String city;

    @NotBlank(message = "Address must be not empty!")
    @Schema(description = "The name of the street/square where the customer lives", example = "Main street 1.")
    private String address;

}
