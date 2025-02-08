package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.dto.incoming.RegisterCommand;
import tutorial.pizzeria.dto.outgoing.CustomerDetails;
import tutorial.pizzeria.dto.outgoing.CustomerListItem;

@Component
public class CustomerMapper {

    public Customer dtoToEntity(RegisterCommand command, String password) {

        Customer customer = new Customer();

        customer.setName(command.getName());
        customer.setEmail(command.getEmail());
        customer.setPassword(password);
        customer.setPhone(command.getPhone());
        customer.setPostalCode(command.getPostalCode());
        customer.setCity(command.getCity());
        customer.setAddress(command.getAddress());

        return new Customer();
    }

    public CustomerDetails entityToDto(Customer customer) {

        CustomerDetails customerDetails = new CustomerDetails();

        customerDetails.setName(customer.getName());
        customerDetails.setEmail(customer.getEmail());
        customerDetails.setAddress(customer.getAddress());
        customerDetails.setPhone(customer.getPhone());

        return customerDetails;
    }

    public CustomerListItem entitiesToDto(Customer customer) {

        CustomerListItem customerListItem = new CustomerListItem();

        customerListItem.setName(customer.getName());
        customerListItem.setEmail(customer.getEmail());

        return customerListItem;
    }
}
