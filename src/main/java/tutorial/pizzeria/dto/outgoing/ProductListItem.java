package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductListItem {

    @Schema(description = "Product's ID", example = "7")
    private Long productId;

    @Schema(description = "Product's name", example = "Hawaii pizza")
    private String name;

    @Schema(description = "Product's price", example = "3190")
    private Double price;
}
