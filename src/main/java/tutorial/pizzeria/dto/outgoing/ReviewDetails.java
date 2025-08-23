package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Recommendation;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDetails {

    @Schema(description = "A couple of words or sentences about the product",
            example = "The pizza was awesome. I recommend this pizzeria to everyone.")
    private String content;

    @Schema(description = "The customer choose between YES or NO. It is mandatory.", example = "YES")
    private Recommendation isRecommend;

    @Schema(description = "The time when the review was written", example = "2025.03.25.")
    private String timeStamp;
}
