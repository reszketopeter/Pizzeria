package tutorial.pizzeria.dto.outgoing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetails {

    private Long orderId;

    private Long customerId;

    private LocalDateTime timeStamp;

    private Integer totalPrice;

    List<ProductDetails> productDetails;

}
