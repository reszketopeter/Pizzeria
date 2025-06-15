package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductModificationCommand {

    @NotBlank(message = "Name must be not empty!")
    private String name;

    @NotBlank(message = "Description must be not empty!")
    private String description;

    @NotNull(message = "Price must be not null!")
    @Positive(message = "Price must be over zero!")
    private Double price;
}
