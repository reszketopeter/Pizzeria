package tutorial.pizzeria.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.outgoing.OrderDetails;
import tutorial.pizzeria.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // This is not the best...Modify!
    @PostMapping
    public ResponseEntity<OrderDetails> createNewOrder(@RequestBody OrderCommand command, HttpServletRequest request) {
        log.info("Create New Order");
        OrderDetails response = orderService.createNewOrder(command, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails> getOrderById(@PathVariable Long id) {
        log.info("Get Order By Id");
        OrderDetails response = orderService.getOrderById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) {
        log.info("Delete Order By Id: {}", id);
        orderService.deleteOrderById(id);
        return new ResponseEntity<>("You successfully deleted the order by id " + id, HttpStatus.OK);
    }
}
