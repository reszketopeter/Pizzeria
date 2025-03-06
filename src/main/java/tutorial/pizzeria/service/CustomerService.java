package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.UserRole;
import tutorial.pizzeria.dto.incoming.RegisterCommand;
import tutorial.pizzeria.dto.mapper.CustomerMapper;
import tutorial.pizzeria.dto.outgoing.CustomerDetails;
import tutorial.pizzeria.dto.outgoing.CustomerListItem;
import tutorial.pizzeria.exception.CustomerNotFoundException;
import tutorial.pizzeria.exception.EmailAlreadyExistsException;
import tutorial.pizzeria.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CustomerService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws CustomerNotFoundException {
        Customer user = customerRepository.findCustomerByEmail(username);
        if (user == null) {
            throw new CustomerNotFoundException("User not found with email: " + username);
        }

        String role = user.getUserRole().getRole();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));


        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    public CustomerDetails register(RegisterCommand command) {
        Customer customer = customerRepository.findByEmail(command.getEmail());
        if (customer != null) {
            throw new EmailAlreadyExistsException("This email is already exist");
        }
        String hashedPassword = passwordEncoder.encode(command.getPassword());
        Customer newCustomer = customerMapper.dtoToEntity(command, hashedPassword);
        customer.setUserRole(UserRole.GUEST);
        customerRepository.save(newCustomer);
        return customerMapper.entityToDto(newCustomer);
    }

    public Long findByName(String username) {
        Customer customerByEmail = customerRepository.findByEmail(username);
        if (customerByEmail == null) {
            throw new CustomerNotFoundException("User not found with email: " + username);
        }
        return customerByEmail.getId();
    }

    public CustomerDetails getCustomer(Long id) {
        Customer customer = findCustomerById(id);
        return customerMapper.entityToDto(customer);
    }

    protected Customer findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with this id: " + id));
        return customer;
    }

    public List<CustomerListItem> findAll() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customerMapper::entitiesToDto)
                .toList();
    }

    public void deleteCustomerById(Long id) {
        Customer customer = findCustomerById(id);
        customerRepository.delete(customer);
    }
}
