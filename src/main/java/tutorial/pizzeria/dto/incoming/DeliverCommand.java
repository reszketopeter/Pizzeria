package tutorial.pizzeria.dto.incoming;

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
    private Long customerId;

    @NotNull(message = "Order id must be not null!")
    private Long OrderId;
}
