package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliverCommand {

    @NotNull(message = "Customer id must be not null!")
    @Schema(description = "Customer's ID", example = "25")
    private Long customerId;

    @NotNull(message = "Order id must be not null!")
    @Schema(description = "Order's ID", example = "67")
    private Long OrderId;
}
