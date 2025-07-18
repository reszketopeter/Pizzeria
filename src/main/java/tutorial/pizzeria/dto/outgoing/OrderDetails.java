package tutorial.pizzeria.dto.outgoing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetails {

    private Long orderId;

    private LocalDate timeStamp;

    private Long customerId;

    private Double orderPriceFT;

    List<OrderItemDetails> orderItemDetails;
}
