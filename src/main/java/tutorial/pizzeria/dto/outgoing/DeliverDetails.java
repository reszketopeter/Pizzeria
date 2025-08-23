package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeliverDetails {

    @Schema(description = "Order's ID", example = "67")
    private Long orderId;

    @Schema(description = "The name of the street/square where the customer lives", example = "Main street 1.")
    private String address;

    @Schema(description = "Customer's name", example = "Test Peter")
    private String customerName;
}
