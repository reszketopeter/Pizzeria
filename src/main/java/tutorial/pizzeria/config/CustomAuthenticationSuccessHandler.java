package tutorial.pizzeria.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tutorial.pizzeria.service.CustomerService;

import java.io.IOException;
import java.rmi.ServerException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CustomerService customerService;

    @Autowired
    public CustomAuthenticationSuccessHandler(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        Long customerId = customerService.findByName(user.getUsername());
        if (customerId == null) {
            throw new ServerException("Customer not found");
        }
        HttpSession session = request.getSession();
        session.setAttribute("customerId", customerId);
        response.sendRedirect("/api/login");
    }
}
