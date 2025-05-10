package tutorial.pizzeria.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.DeliverCommand;
import tutorial.pizzeria.dto.incoming.OrderCommand;
import tutorial.pizzeria.dto.outgoing.DeliverDetails;
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

    @PostMapping("product/{productId}")
    public ResponseEntity<OrderDetails> createNewOrder(@RequestBody @Valid OrderCommand command,
                                                       @PathVariable Long productId, HttpServletRequest request) {
        log.info("Create New Order");
        OrderDetails response = orderService.createNewOrder(command, productId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/deliver")
    public ResponseEntity<DeliverDetails> deliverOrder(@RequestBody @Valid DeliverCommand command) {
        log.info("Delivering to user id: {} order id: {}", command.getCustomerId(), command.getOrderId());
        DeliverDetails details = orderService.updateToDelivered(command);
        log.debug("Delivering was successful: {}", details);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PutMapping("/cancel")
    public ResponseEntity<String> cancelOrder(HttpServletRequest req) {
        log.info("Cancel order from customer with id: {}", req.getSession().getAttribute("customerId"));
        String response = orderService.cancelingOrder(req);
        log.debug("Order was canceled: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
