package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Category;

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
    private Integer price;

    @NotNull(message = "Category ID must be not null!")
    private Long categoryID;
}
