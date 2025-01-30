package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.dto.incoming.LoginCommand;
import tutorial.pizzeria.exception.CustomerNotRegisteredYetException;
import tutorial.pizzeria.exception.InvalidPasswordException;
import tutorial.pizzeria.repository.CustomerRepository;

@Service
@Transactional
public class LoginService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(LoginCommand command) {
        Customer customer = customerRepository.findByEmail(command.getEmail());
        if (customer == null) {
            throw new CustomerNotRegisteredYetException("You have not registered yet!");
        }
        if (!passwordEncoder.matches(command.getPassword(), customer.getPassword())) {
            throw new InvalidPasswordException("Invalid Password");
        }

    }
}
