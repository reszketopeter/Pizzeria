package tutorial.pizzeria.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.*;
import tutorial.pizzeria.dto.incoming.DeliverCommand;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.mapper.OrderMapper;
import tutorial.pizzeria.dto.outgoing.DeliverDetails;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.exception.CustomerIdIsNullException;
import tutorial.pizzeria.exception.OrderNotFoundException;
import tutorial.pizzeria.exception.ProductIsCurrentlyNotAvailableException;
import tutorial.pizzeria.exception.ProductNotFoundException;
import tutorial.pizzeria.repository.OrderItemRepository;
import tutorial.pizzeria.repository.OrderRepository;
import tutorial.pizzeria.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Log
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final CustomerService customerService;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository, CustomerService customerService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.customerService = customerService;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderDetails createNewOrder(OrderCommand command, Long productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null) {
            throw new CustomerIdIsNullException("The customer id is null. First of all you have to login!");
        }
        Product product = findProductById(productId);
        if (!product.getIsAvailable())
            throw new ProductIsCurrentlyNotAvailableException("Apologise, but this product is not available now.");
        Order order = orderGuard(session, customerId);
        orderRepository.save(order);
        OrderItem orderItem = makeOrderItem(command, productId, order);
        order.getOrderItems().add(orderItem);
        return orderMapper.entityToDto(order, order.getOrderItems());
    }

    private OrderItem makeOrderItem(OrderCommand command, Long productId, Order order) {
        Product product = findProductById(productId);
        OrderItem orderItem = orderMapper.makeOrderItem(order, product, command);
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    protected Order orderGuard(HttpSession session, Long customerId) {
        Order orderToFind = orderRepository.findByCustomerId(customerId);
        return orderToFind == null ? makeOrderAndSetSessionAttribute(session, customerId) : orderToFind;
    }

    private Order makeOrderAndSetSessionAttribute(HttpSession session, Long customerId) {
        Order order = makeOrder(customerId);
        session.setAttribute("orderId", order.getId());
        return order;
    }

    private Order makeOrder(Long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        Order order = orderMapper.makeOrder(customer);
        orderRepository.save(order);
        return order;
    }

    protected Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("No product with this id in the database!"));
    }

    public OrderDetails getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Sorry, there is not any Order with this id: " + id));
        List<OrderItem> orderItems = orderItemRepository.getOrderItemsWithOrderId(order.getId());
        return orderMapper.entityToDto(order, orderItems);
    }

    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Sorry, there is not any Order with this id: " + id));
        orderRepository.delete(order);
    }

    public String cancelingOrder(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Long customerId = (Long) session.getAttribute("customerId");
        Order order = orderRepository.findByCustomerId(customerId);
        if (order == null) {
            throw new OrderNotFoundException(customerId);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return "Your order has cancelled";
    }

    public DeliverDetails updateToDelivered(@Valid DeliverCommand command) {
        Order order = orderRepository.getOrderById(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Sorry, we didn't find any order with this id "
                        + command.getOrderId() + "in the system"));
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setTimeStamp(LocalDate.now());
        return orderMapper.makeDeliverDetails(order);
    }
}
