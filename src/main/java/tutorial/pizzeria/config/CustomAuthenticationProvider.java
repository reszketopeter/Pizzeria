package tutorial.pizzeria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        try {
//            UserDetails user = customerService.loadUserByUsername(username);
//            if (passwordEncoder.matches(password, user.getPassword())) {
//                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
//            } else {
//                throw new CustomerNotRegisteredYetException("Invalid password");
//            }
//        } catch (CustomerNotRegisteredYetException e) {
//            throw new CustomerNotRegisteredYetException("User not found with email: " + username);
//        }
//    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

