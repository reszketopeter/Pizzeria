package tutorial.pizzeria.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.domain.OrderStatus;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.outgoing.DeliverDetails;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.dto.outgoing.ProductDetails;

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

    public OrderDetails entityToDto(Order order, List<Product> products) {

        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setCustomerId(order.getCustomer().getId());
        orderDetails.setOrderId(order.getId());
        orderDetails.setTimeStamp(order.getTimeStamp());
        orderDetails.setTotalPrice(order.getTotalPrice());
        orderDetails.setCustomerId(order.getCustomer().getId());
//        orderDetails.setProductDetails(makeProductDetailsList(products));

        return orderDetails;
    }

    // OrderItem? Is it necessary?
//    private List<ProductDetails> makeProductDetailsList(List<Product> products) {
//
//        return products.stream()
//                .map(this::makeOrderDetails)
//                .toList();
//    }
//
//    public OrderDetails makeOrderItemDetails(OrderItem orderItem) {
//
//        OrderItemDetails orderItemDetails = new OrderItemDetails();
//
//        orderItemDetails.setProductName(orderItem.getProduct().getName());
//        orderItemDetails.setQuantity(orderItem.getQuantity());
//        orderItemDetails.setProductId(orderItem.getProduct().getId());
//
//        return orderItemDetails;
//    }

    // It is too complicated.
//    public Order dtoToEntity (OrderCommand command) {
//
//        Order order = new Order();
//    }


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

