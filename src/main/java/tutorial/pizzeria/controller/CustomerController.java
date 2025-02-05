package tutorial.pizzeria.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.RegisterCommand;
import tutorial.pizzeria.dto.outgoing.CustomerDetails;
import tutorial.pizzeria.service.CustomerService;

@RestController
@Slf4j
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterCommand command) {
        log.info("Post register customer {}", command);
        customerService.register(command);
        return new ResponseEntity<>("You have successfully registered", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetails> getCustomer(@PathVariable Long id) {
        log.info("Get Customer by id: {}", id);
        CustomerDetails response = customerService.getCustomer(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
