package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Customer;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewListItemWithTime {

    private String content;

    private LocalDateTime timestamp;

}
