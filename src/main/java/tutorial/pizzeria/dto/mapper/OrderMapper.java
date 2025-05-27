package tutorial.pizzeria.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.domain.OrderItem;
import tutorial.pizzeria.domain.OrderStatus;
import tutorial.pizzeria.dto.outgoing.DeliverDetails;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.dto.outgoing.OrderItemDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    private final ProductMapper productMapper;

    @Autowired
    public OrderMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public OrderDetails entityToDto(Order order, List<OrderItem> orderItems) {

        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setCustomerId(order.getCustomer().getId());
        orderDetails.setOrderId(order.getId());
        orderDetails.setCustomerId(order.getCustomer().getId());
        orderDetails.setOrderItemDetails(makeOrderItemDetailsList(orderItems));

        return orderDetails;
    }

    private List<OrderItemDetails> makeOrderItemDetailsList(List<OrderItem> orderItems) {

        return orderItems.stream()
                .map(this::makeOrderItemDetails)
                .toList();
    }

    public OrderItemDetails makeOrderItemDetails(OrderItem orderItem) {

        OrderItemDetails orderItemDetails = new OrderItemDetails();

        orderItemDetails.setProductName(orderItem.getProduct().getName());
        orderItemDetails.setQuantity(orderItem.getQuantity());
        orderItemDetails.setProductId(orderItem.getProduct().getId());

        return orderItemDetails;
    }


    public Order makeOrder(Customer customer) {

        Order order = new Order();

        order.setCustomer(customer);
        order.setTimeStamp(LocalDateTime.now());
        order.setCity(customer.getCity());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setProducts(new ArrayList<>());

        return order;
    }

    public DeliverDetails makeDeliverDetails(Order order) {

        DeliverDetails deliverDetails = new DeliverDetails();

        deliverDetails.setOrderId(order.getId());
        // Address is not the best like this.
        deliverDetails.setAddress(order.getCustomer().getAddress());
        deliverDetails.setCustomerName(order.getCustomer().getName());

        return deliverDetails;
    }
}

