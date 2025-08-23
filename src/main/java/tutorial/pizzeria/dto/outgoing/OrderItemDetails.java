package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetails {

    @Schema(description = "Product's ID", example = "7")
    private Long productId;

    @Schema(description = "Product's name", example = "Hawaii pizza")
    private String productName;

    @Schema(description = "The number of the product which the customer would like to order.", example = "2")
    private Integer quantity;

}
