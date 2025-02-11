package tutorial.pizzeria.dto.outgoing;

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

    private String content;

    private Recommendation isRecommend;

    private String timeStamp;
}
