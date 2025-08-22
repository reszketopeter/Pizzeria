package tutorial.pizzeria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tutorial.pizzeria.service.CustomerService;

import static tutorial.pizzeria.domain.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomerService customerService;
    private final PasswordEncoder passwordencoder;

    @Autowired
    public SecurityConfig(CustomerService customerService, PasswordEncoder passwordencoder) {
        this.customerService = customerService;
        this.passwordencoder = passwordencoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/debug/auth").hasAuthority(ADMIN.name())
                        .anyRequest().permitAll()

                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response,
                                              customAccessDeniedException) -> {
                            response.setStatus(403);
                            response.getWriter().write("Access denied! Sorry, you haven't got permission " +
                                    "to access this page!");
                        })
                        .authenticationEntryPoint((request, response,
                                                   authException) -> {
                            response.setStatus(401);
                            response.getWriter().write("Unauthorized access! Please register/login first! " +
                                    authException.getMessage());
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
