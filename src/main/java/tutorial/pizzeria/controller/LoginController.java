package tutorial.pizzeria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.LoginCommand;
import tutorial.pizzeria.exception.CustomerNotFoundException;
import tutorial.pizzeria.exception.InvalidPasswordException;
import tutorial.pizzeria.service.LoginService;

@Tag(name = "Authentication API", description = "Endpoints for customer login, logout, and authentication debugging")
@RestController
@Slf4j
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(
            summary = "Customer login",
            description = "Authenticates a customer using email and password. Stores customer ID in session upon success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "422", description = "Invalid password"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginCommand command, HttpServletRequest request) {
        log.info("Post login customer with {}", command);
        try {
            Long customerId = loginService.login(command, request);
            HttpSession session = request.getSession();
            session.setAttribute("customerId", customerId);
            return new ResponseEntity<>("You have successfully logged in!", HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(
            summary = "Customer logout",
            description = "Invalidates the current session and clears the security context.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.info("Post logout customer");
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("You have successfully logged out!", HttpStatus.OK);
    }

    @Operation(
            summary = "Debug authentication",
            description = "Returns current authentication details and session-bound customer ID. " +
                    "For development use only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication details retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/debug/auth")
    public ResponseEntity<String> debugAuth(HttpSession session) {
        log.info("Current Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        log.info("Debugging authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder response = new StringBuilder("User: " + authentication.getName() + "\nAuthorities: ");
        authentication.getAuthorities().
                forEach(auth -> response.append(auth.getAuthority()).append("\n"));
        response.append(session.getAttribute("customerId"));
        return ResponseEntity.ok(response.toString());
    }
}
