package tutorial.pizzeria.dto.outgoing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetails {

    private String name;

    private String email;

    private String phone;

    private Integer postalCode;

    private String city;

    private String address;
}
