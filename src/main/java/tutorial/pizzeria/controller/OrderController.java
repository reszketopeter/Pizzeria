package tutorial.pizzeria.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

//    @PostMapping
//    public ResponseEntity<OrderDetails> createNewOrder (@RequestBody OrderCommand command){
//      log.info("Create New Order");
//      orderService.createNewOrder(command);
//    }

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
