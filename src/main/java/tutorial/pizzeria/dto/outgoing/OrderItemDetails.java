package tutorial.pizzeria.dto.outgoing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDetails {

    private Long productId;

    private String productName;

    private Integer quantity;

}
