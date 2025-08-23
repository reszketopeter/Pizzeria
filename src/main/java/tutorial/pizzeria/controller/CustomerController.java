package tutorial.pizzeria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.CustomerUpdateCommand;
import tutorial.pizzeria.dto.incoming.RegisterCommand;
import tutorial.pizzeria.dto.outgoing.CustomerDetails;
import tutorial.pizzeria.dto.outgoing.CustomerListItem;
import tutorial.pizzeria.service.CustomerService;

import java.util.List;

@Tag(name = "Customer API", description = "Customer manager operations: registration, searching, modification, delete")
@RestController
@Slf4j
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "New customer registration",
            description = "The new customer is registered based on the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful registration"),
            @ApiResponse(responseCode = "400", description = "Wrong input"),
            @ApiResponse(responseCode = "500", description = "Server failure")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterCommand command) {
        log.info("Post register customer {}", command);
        customerService.register(command);
        return new ResponseEntity<>("You have successfully registered!", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Returns detailed information about a customer based on their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetails> getCustomer(@PathVariable("id") Long id) {
        log.info("Get Customer by id: {}", id);
        CustomerDetails response = customerService.getCustomer(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all customers",
            description = "Returns a list of all registered customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<CustomerListItem>> getAllCustomer() {
        log.info("Get All Customers");
        List<CustomerListItem> customers = customerService.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Operation(
            summary = "Update customer email",
            description = "Updates the email address of a customer based on their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer email updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDetails> updateCustomer(@PathVariable("id") Long id,
                                                          @RequestBody @Valid CustomerUpdateCommand command) {
        log.info("Change Customer's email");
        CustomerDetails response = customerService.upgradeEmail(id, command);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete customer by ID",
            description = "Deletes a customer from the system based on their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id) {
        log.info("Delete Customer By Id: {}", id);
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>("You deleted the customer with id: " + id, HttpStatus.OK);
    }


}
