package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.Recommendation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommand {

    @Schema(description = "Customer's name", example = "Test Peter")
    private Customer customer;

    @Schema(description = "A couple of words or sentences about the product",
            example = "The pizza was awesome. I recommend this pizzeria to everyone.")
    private String content;

    @NotNull(message = "Please select YES/NO!")
    @Schema(description = "The customer choose between YES or NO. It is mandatory.", example = "YES")
    private Recommendation isRecommend;
}
