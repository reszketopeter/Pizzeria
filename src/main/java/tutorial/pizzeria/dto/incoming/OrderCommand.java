package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCommand {

    @Min(value = 1, message = "The quantity must be greater than 0")
    @NotNull(message = "Quantity must not be null")
    private Integer quantity;

}
