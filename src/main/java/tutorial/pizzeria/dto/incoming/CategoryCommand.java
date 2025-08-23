package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryCommand {

    @NotBlank(message = "Name must be not empty!")
    @Schema(description = "Category's name", example = "Desserts")
    private String name;

    @NotBlank(message = "Description must be not empty!")
    @Schema(description = "Description about the category", example = "Pancakes, ice-cream...")
    private String description;
}
