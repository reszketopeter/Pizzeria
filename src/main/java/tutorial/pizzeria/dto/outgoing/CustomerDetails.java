package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetails {

    @Schema(description = "Customer's name", example = "Test Peter")
    private String name;

    @Schema(description = "Customer's email address", example = "test.peter@gmail.com")
    private String email;

    @Schema(description = "Customer's phone number", example = "+362586987")
    private String phone;

    @Schema(description = "Customer's postal code", example = "1039 or 21547")
    private Integer postalCode;

    @Schema(description = "The name of the city/village where the customer lives", example = "London")
    private String city;

    @Schema(description = "The name of the street/square where the customer lives", example = "Main street 1.")
    private String address;
}
