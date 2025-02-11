package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Recommendation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommand {

    private String content;

    @NotNull(message = "Please select YES/NO!")
    private Recommendation isRecommend;
}
