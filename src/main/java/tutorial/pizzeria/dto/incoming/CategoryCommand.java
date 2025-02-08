package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryCommand {

    @NotBlank(message = "Name must be not empty!")
    private String name;

    @NotBlank(message = "Name must be not empty!")
    private String description;
}
