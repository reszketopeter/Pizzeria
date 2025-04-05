package tutorial.pizzeria.controller;

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
import tutorial.pizzeria.service.LoginService;

@RestController
@Slf4j
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginCommand command, HttpServletRequest request) {
        log.info("Post login customer with {}", command);
        try {
            Long customerId = loginService.login(command);
            HttpSession session = request.getSession();
            session.setAttribute("customerId", customerId);
            return new ResponseEntity<>("You have successfully logged in!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.info("Post logout customer");
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("You have successfully logged out!", HttpStatus.OK);
    }

    @GetMapping("/debug/auth")
    public ResponseEntity<String> debugAuth(HttpSession session) {
        log.info("Debugging authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder response = new StringBuilder("User: " + authentication.getName() + "Authorities: ");
        authentication.getAuthorities().
                forEach(auth -> response.append(auth.getAuthority()).append("\n"));
        response.append(session.getAttribute("customerId"));
        return ResponseEntity.ok(response.toString());
    }
}
