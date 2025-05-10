package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeliverDetails {

    private Long orderId;

    private String address;

    private String customerName;
}
