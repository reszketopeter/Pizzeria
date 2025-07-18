package tutorial.pizzeria.dto.incoming;

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
    private String name;

    @NotBlank(message = "Description must be not empty!")
    private String description;

    @NotNull(message = "Price must be not null!")
    @Positive(message = "Price must be positive!")
    private Double price;

    @NotNull(message = "Category ID must be not null!")
    private Long categoryId;
}
