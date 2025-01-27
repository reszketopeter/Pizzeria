package tutorial.pizzeria.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorial.pizzeria.dto.incoming.RegisterCommand;
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


}
