package tutorial.pizzeria.dto.outgoing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetails {

    private LocalDateTime timeStamp;

    private Integer totalPrice;

}
