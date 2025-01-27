package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.UserRole;
import tutorial.pizzeria.dto.incoming.RegisterCommand;
import tutorial.pizzeria.dto.mapper.CustomerMapper;
import tutorial.pizzeria.dto.outgoing.CustomerDetails;
import tutorial.pizzeria.exception.EmailAlreadyExistsException;
import tutorial.pizzeria.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDetails register(RegisterCommand command) {
        String byEmail = customerRepository.findByEmail(command.getEmail());
        if (byEmail != null) {
            throw new EmailAlreadyExistsException("This email is already exist");
        }
        String hashedPassword = passwordEncoder.encode(command.getPassword());
        Customer customer = customerMapper.dtoToEntity(command, hashedPassword);
        customer.setUserRole(UserRole.GUEST);
        customerRepository.save(customer);
        return customerMapper.entityToDto(customer);
    }
}
