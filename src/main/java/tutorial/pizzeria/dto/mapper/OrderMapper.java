package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.dto.outgoing.OrderDetails;

@Component
public class OrderMapper {

    //ezen még alakítani
    public OrderDetails entityToDto(Order order) {

        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setTimeStamp(order.getTimeStamp());
        orderDetails.setTotalPrice(order.getTotalPrice());

        return orderDetails;
    }
}
