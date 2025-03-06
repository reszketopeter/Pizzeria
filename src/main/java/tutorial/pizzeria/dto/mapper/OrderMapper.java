package tutorial.pizzeria.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.domain.OrderStatus;
import tutorial.pizzeria.domain.Product;
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

        orderDetails.setTimeStamp(order.getTimeStamp());
        orderDetails.setTotalPrice(order.getTotalPrice());
        orderDetails.setCustomerId(order.getCustomer().getId());
        orderDetails.setOrderId(order.getId());
//        orderDetails.setProductDetails(makeProductDetailsList(products));

        return orderDetails;
    }


//    private List<ProductDetails> makeProductDetailsList(List<Product> products) {
//        return products.stream()
//                .map(productMapper::entitiesToDto)
//                .toList();
//
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
}

