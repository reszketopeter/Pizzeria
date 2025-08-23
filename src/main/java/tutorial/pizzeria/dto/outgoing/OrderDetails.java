package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    @Schema(description = "Customer's ID", example = "25")
    private Long customerId;

    @Schema(description = "Order's ID", example = "67")
    private Long orderId;

    @Schema(description = "The time when the order was happened", example = "2025.03.25.")
    private LocalDate timeStamp;

    @Schema(description = "Order's total price", example = "8190")
    private Double orderPriceFT;

    @Schema(description = "A list of the product's ID, name and quantity", example = "7, Salami pizza, 1")
    List<OrderItemDetails> orderItemDetails;
}
