package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {

    @Schema(description = "Product's ID", example = "7")
    private Long productId;

    @Schema(description = "Product's name", example = "Hawaii pizza")
    private String name;

    @Schema(description = "A description about the product",
            example = "Pizza with tomato sauce, ham, cheese und pineapple")
    private String description;

    @Schema(description = "Product's price", example = "3190")
    private Double price;

    @Schema(description = "The category's name to which the product belongs", example = "Desserts")
    private String categoryName;
}
