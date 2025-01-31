package tutorial.pizzeria.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<String> login(@RequestBody LoginCommand command) {
        log.info("Post login customer with {}", command);
        loginService.login(command);
        return new ResponseEntity<>("You have successfully logged in!", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.info("Post logout customer");
        HttpSession session = request.getSession();
        session.invalidate();
        return new ResponseEntity<>("You have successfully logged out", HttpStatus.OK);
    }
}
