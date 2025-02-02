package tutorial.pizzeria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tutorial.pizzeria.exception.CustomerNotFoundException;
import tutorial.pizzeria.service.CustomerService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;

    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, CustomerService customerService) {
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        try {
            UserDetails user = customerService.loadUserByUsername(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            } else {
                throw new CustomerNotFoundException("Invalid password");
            }
        } catch (CustomerNotFoundException e) {
            throw new CustomerNotFoundException("User not found with email: " + username);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

