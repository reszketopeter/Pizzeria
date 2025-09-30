package tutorial.pizzeria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Order API", description = "Operations related to order creation, retrieval, update and deletion")
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Create new order",
            description = "Creates a new order for the specified product and customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{productId}")
    public ResponseEntity<OrderDetails> createNewOrder(@Valid @RequestBody OrderCommand command,
                                                       @PathVariable Long productId, HttpServletRequest request) {
        log.info("Create New Order");
        OrderDetails response = orderService.createNewOrder(command, productId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Mark order as delivered",
            description = "Updates the status of an order to 'delivered' for the specified customer and order ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as delivered"),
            @ApiResponse(responseCode = "400", description = "Invalid delivery data"),
            @ApiResponse(responseCode = "404", description = "Order or customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/deliver")
    public ResponseEntity<DeliverDetails> deliverOrder(@Valid @RequestBody DeliverCommand command) {
        log.info("Delivering to user id: {} order id: {}", command.getCustomerId(), command.getOrderId());
        DeliverDetails details = orderService.updateToDelivered(command);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @Operation(
            summary = "Cancel order",
            description = "Cancels the current customer's active order based on session data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully canceled"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/cancel")
    public ResponseEntity<String> cancelOrder(HttpServletRequest req) {
        log.info("Cancel order from customer with id: {}", req.getSession().getAttribute("customerId"));
        String response = orderService.cancelingOrder(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Get order by ID",
            description = "Retrieves detailed information about an order based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails> getOrderById(@PathVariable Long id) {
        log.info("Get Order By Id");
        OrderDetails response = orderService.getOrderById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete order by ID",
            description = "Deletes an order from the system based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) {
        log.info("Delete Order By Id: {}", id);
        orderService.deleteOrderById(id);
        return new ResponseEntity<>("You successfully deleted the order by id " + id, HttpStatus.OK);
    }
}
