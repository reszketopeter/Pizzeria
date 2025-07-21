package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    private Long customerId;

    private Long orderId;

    private LocalDate timeStamp;

    private Double orderPriceFT;

    List<OrderItemDetails> orderItemDetails;
}
