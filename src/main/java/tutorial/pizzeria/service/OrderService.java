package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.mapper.OrderMapper;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.exception.OrderNotFoundException;
import tutorial.pizzeria.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

//    public OrderDetails createNewOrder(OrderCommand command) {
//    }

    public OrderDetails getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Sorry, ther is not any Order with this id: " + id));
        return orderMapper.entityToDto(order);
    }

    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Sorry, ther is not any Order with this id: " + id));
        orderRepository.delete(order);
    }
}
