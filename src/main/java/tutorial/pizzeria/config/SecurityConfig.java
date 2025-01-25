package tutorial.pizzeria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import tutorial.pizzeria.service.CustomerService;

import static tutorial.pizzeria.domain.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomerService customerService;
    private final PasswordEncoder passwordencoder;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    @Lazy
    public SecurityConfig(CustomerService customerService, PasswordEncoder passwordencoder,
                          CustomAuthenticationSuccessHandler successHandler) {
        this.customerService = customerService;
        this.passwordencoder = passwordencoder;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/api/login")
                        .successHandler(successHandler)
                        .failureHandler(customAuthenticationFailureHandler())
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("api/debug/auth").hasAuthority(ADMIN.name())
                        .anyRequest().permitAll()

                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, customAccessDeniedException) -> {
                            response.setStatus(403);
                            response.getWriter().write("Access denied! Sorry, you haven't got permission " +
                                    "to access this page!");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.getWriter().write("Unauthorized access! Please register first!");
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomerService customerService) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder
//                .userDetailsService(customerService)
//                .passwordEncoder(passwordencoder);
//        return authenticationManagerBuilder.build();
        return null;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return null;
//        return new CustomAuthenticationProvider(customerService, passwordencoder);
    }


    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
//        return (request, response, exception) -> {
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//
//            if (exception instanceof CustomerNotRegisteredYetException) {
//                response.setStatus(HttpStatus.BAD_REQUEST.value());
//                response.getWriter().write("{\"error\": \"User not found with email: " +
//                        request.getParameter("email") + "\"}");
//            } else if (exception instanceof BadCredentialsException) {
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                response.getWriter().write("{\"error\": \"Invalid username or password\"}");
//            } else {
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                response.getWriter().write("{\"error\": \"Unauthorized access! Please register first!\"}");
//            }
//        };
//
//    }
        return null;
    }
}
