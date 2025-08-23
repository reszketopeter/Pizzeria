package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDetails {

    @Schema(description = "Category's name", example = "Desserts")
    private String name;

    @Schema(description = "Description about the category", example = "Pancakes, ice-cream...")
    private String description;
}
