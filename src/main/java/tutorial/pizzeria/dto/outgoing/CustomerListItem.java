package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListItem {

    @Schema(description = "Customer's name", example = "Test Peter")
    private String name;

    @Schema(description = "Customer's email address", example = "test.peter@gmail.com")
    private String email;
}
