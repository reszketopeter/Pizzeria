package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Customer;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReviewListItem {

    private Customer customer;

    private String content;
}
