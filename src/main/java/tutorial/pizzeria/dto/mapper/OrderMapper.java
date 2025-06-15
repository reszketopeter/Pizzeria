package tutorial.pizzeria.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.*;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.outgoing.DeliverDetails;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.dto.outgoing.OrderItemDetails;
import tutorial.pizzeria.exception.ProductNotFoundException;
import tutorial.pizzeria.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderMapper(ProductMapper productMapper, OrderRepository orderRepository) {
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
    }

    public OrderDetails entityToDto(Order order, List<OrderItem> orderItems) {

        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setCustomerId(order.getCustomer().getId());
        orderDetails.setOrderId(order.getId());
        orderDetails.setOrderPriceFT(order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getValue())
                .sum());
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

        orderItemDetails.setProductId(orderItem.getProduct().getId());
        orderItemDetails.setProductName(orderItem.getProduct().getName());
        orderItemDetails.setQuantity(orderItem.getQuantity());

        return orderItemDetails;
    }


    public Order makeOrder(Customer customer) {

        Order order = new Order();

        order.setCustomer(customer);
        order.setTimeStamp(LocalDateTime.now());
        order.setCity(customer.getCity());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());
//        order.setTotalPrice(order.getOrderItems().stream()
//                .mapToDouble(item -> item.getQuantity() * item.getValue())
//                .sum());

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

    public OrderItem makeOrderItem(Order order, Product product, OrderCommand command) {

        OrderItem orderItem = new OrderItem();

        orderItem.setName(product.getName());
        orderItem.setQuantity(command.getQuantity());
        orderItem.setValue(product.getPrice());
        orderItem.setOrder(order);
        orderItem.setProduct(product);

        return orderItem;
    }
}

