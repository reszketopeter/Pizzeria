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
public class ProductModificationCommand {

    @NotBlank(message = "Name must be not empty!")
    @Schema(description = "Product's original name", example = "Hawaii pizza")
    private String originalName;

    //    @NotBlank(message = "Name must be not empty!")
    @Schema(description = "Product's new name", example = "Pizza with pineapple")
    private String newName;

    @NotBlank(message = "Description must be not empty!")
    @Schema(description = "A description about the product",
            example = "Pizza with tomato sauce, ham, cheese und pineapple")
    private String description;

    @NotNull(message = "Price must be not null!")
    @Positive(message = "Price must be over zero!")
    @Schema(description = "Product's price", example = "3190")
    private Double price;
}
