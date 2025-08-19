package tutorial.pizzeria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.dto.incoming.LoginCommand;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void givenARegisteredCustomerAndAValidLoginCommand_whenLogin_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCustomer();

        LoginCommand command = new LoginCommand();
        command.setEmail("test@email.com");
        command.setPassword("test1Password");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(result ->
                        result.getResponse().getContentAsString().contains("You have successfully logged in!"));
    }

    @Test
    void givenARegisteredCustomerAndAnEmptyEmail_whenLogin_thenReturnAnUnprocessableEntityStatus()
            throws Exception {

        saveCustomer();

        LoginCommand command = new LoginCommand();
        command.setEmail("");
        command.setPassword("test1Password");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenARegisteredCustomerAndAnEmptyPassword_whenLogin_thenReturnAnUnprocessableEntityStatus()
            throws Exception {

        saveCustomer();

        LoginCommand command = new LoginCommand();
        command.setEmail("test@email.com");
        command.setPassword("");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenARegisteredCustomerAndAWrongEmail_whenLogin_thenReturnNotFoundStatus() throws Exception {

        saveCustomer();

        LoginCommand command = new LoginCommand();
        command.setEmail("tes@email.com");
        command.setPassword("test1Password");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> result.getResponse().getContentAsString().contains(
                        "You have not registered yet!"));
    }

    @Test
    void givenARegisteredCustomerAndAWrongPassword_whenLogin_thenReturnAnUnprocessableEntityStatus()
            throws Exception {

        saveCustomer();

        LoginCommand command = new LoginCommand();
        command.setEmail("test@email.com");
        command.setPassword("testPassword");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> result.getResponse().getContentAsString().contains("Invalid password!"));
    }

    @Test
    @WithMockUser(username = "test2@email.com", authorities = "ADMIN")
    void givenAnAdminWhoAreLoggedIn_whenDebugAuth_thenReturnTheAuthenticationAndAuthoritiesAndTheCustomerId()
            throws Exception {

        saveAdmin();

        mockMvc.perform(get("/api/debug/auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("2"));
    }

    @Test
    @WithMockUser(username = "test@email.com", authorities = "GUEST")
    void givenACustomerWhorAreLoggedIn_whenDebugAuth_thenReturn403Status() throws Exception {

        saveCustomer();

        mockMvc.perform(get("/api/debug/auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenAnAdminWhoAreNotLoggedIn_whenDebugAuth_thenReturn401Status() throws Exception {

        saveAdmin();

        mockMvc.perform(get("/api/debug/auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> result.getResponse().getContentAsString()
                        .contains("Unauthorized access! Please register/login first!"));
    }

    @Test
    @WithMockUser(username = "test@email.com", authorities = "GUEST")
    void givenACustomerWhoAreLoggedIn_whenLogOut_thenReturnOkStatusAndTheResponse() throws Exception {

        saveCustomer();

        mockMvc.perform(post("/api/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString()
                        .contains("You have successfully logged out!"));
    }

    private void saveCustomer() {
        String hashedPassword = passwordEncoder.encode("test1Password");
        entityManager.createNativeQuery("INSERT INTO customer" +
                        "(id, name, password, email, phone, postal_code, city, address, role)" +
                        "VALUES (1, 'Test Elek', :password, 'test@email.com','+36123456',1234," +
                        "'Test city','Test street 22','GUEST')")
                .setParameter("password", hashedPassword)
                .executeUpdate();
    }

    private void saveAdmin() {
        String hashedPassword = passwordEncoder.encode("test2Password");
        entityManager.createNativeQuery("INSERT INTO customer" +
                        "(id, name, password, email, phone, postal_code, city, address, role)" +
                        "VALUES (2, 'Test Admin', :password, 'test2@email.com','+36123457',1234," +
                        "'Test city','Test street 23','ADMIN')")
                .setParameter("password", hashedPassword)
                .executeUpdate();
    }
}
