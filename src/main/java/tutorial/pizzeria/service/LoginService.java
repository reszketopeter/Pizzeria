package tutorial.pizzeria.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.dto.incoming.LoginCommand;
import tutorial.pizzeria.exception.CustomerNotFoundException;
import tutorial.pizzeria.exception.InvalidPasswordException;
import tutorial.pizzeria.repository.CustomerRepository;

@Service
@Transactional
@Slf4j
public class LoginService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Long login(LoginCommand command, HttpServletRequest request) {
        Customer customer = customerRepository.findByEmail(command.getEmail());
        if (customer == null) {
            throw new CustomerNotFoundException("You have not registered yet!");
        }
        if (!passwordEncoder.matches(command.getPassword(), customer.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(command.getEmail(), command.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        log.info("Current Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Save SecurityContext to session
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return customer.getId();
    }
}
