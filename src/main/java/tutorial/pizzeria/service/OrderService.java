package tutorial.pizzeria.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.domain.OrderStatus;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.mapper.OrderMapper;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.exception.CustomerIdIsNullException;
import tutorial.pizzeria.exception.OrderNotFoundException;
import tutorial.pizzeria.exception.ProductNotFoundException;
import tutorial.pizzeria.repository.OrderRepository;
import tutorial.pizzeria.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final CustomerService customerService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.customerService = customerService;
    }

    public OrderDetails createNewOrder(OrderCommand command, Long productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null) {
            throw new CustomerIdIsNullException("The customer id is null you have to login first!");
        }
        Product product = findProductById(productId);
        Order order = orderGuard(session, customerId);
        List<Product> products = orderRepository.getProductsWithOrderId(order.getId());
        return orderMapper.entityToDto(order, products);
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
        List<Product> products = orderRepository.getProductsWithOrderId(order.getId());
        return orderMapper.entityToDto(order, products);
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
}
