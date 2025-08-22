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

    @Operation(summary = "New customer registration", description = "The new customer is registered based on the data.")
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

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetails> getCustomer(@PathVariable("id") Long id) {
        log.info("Get Customer by id: {}", id);
        CustomerDetails response = customerService.getCustomer(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CustomerListItem>> getAllCustomer() {
        log.info("Get All Customers");
        List<CustomerListItem> customers = customerService.findAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDetails> updateCustomer(@PathVariable("id") Long id,
                                                          @RequestBody @Valid CustomerUpdateCommand command) {
        log.info("Change Customer's email");
        CustomerDetails response = customerService.upgradeEmail(id, command);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id) {
        log.info("Delete Customer By Id: {}", id);
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>("You deleted the customer with id: " + id, HttpStatus.OK);
    }


}
