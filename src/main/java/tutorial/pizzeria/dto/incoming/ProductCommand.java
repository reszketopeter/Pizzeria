package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCommand {

    @NotBlank(message = "Name must be not empty!")
    @Schema(description = "Product's name", example = "Hawaii pizza")
    private String name;

    @NotBlank(message = "Description must be not empty!")
    @Schema(description = "A description about the product",
            example = "Pizza with tomato sauce, ham, cheese und pineapple")
    private String description;

    @NotNull(message = "Price must be not null!")
    @Positive(message = "Price must be positive!")
    @Schema(description = "Product's price", example = "3190")
    private Double price;

    @NotNull(message = "Category ID must be not null!")
    @Schema(description = "The category's ID to which the product belongs", example = "5")
    private Long categoryId;
}
