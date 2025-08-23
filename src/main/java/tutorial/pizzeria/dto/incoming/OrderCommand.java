package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCommand {

    @Min(value = 1, message = "The quantity must be greater than 0!")
    @NotNull(message = "Quantity must not be null!")
    @Schema(description = "The number of the product which the customer would like to order.", example = "2")
    private Integer quantity;

}
