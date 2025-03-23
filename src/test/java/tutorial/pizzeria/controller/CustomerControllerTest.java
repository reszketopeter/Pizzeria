package tutorial.pizzeria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.dto.incoming.RegisterCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenValidRegisterCommand_whenRegister_thenReturnsCreatedStatus() throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> assertEquals(
                        "You have successfully registered!",
                        result.getResponse().getContentAsString()))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithEmptyName_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Name must be not empty!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithInvalidPassword_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("testPassword");
        command.setEmail("test@test.com");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "The password must contain at least one lowercase letter, one uppercase letter, " +
                                "one number and must be at least 8 characters long!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithEmptyPassword_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("");
        command.setEmail("test@test.com");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Password must be not empty!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithEmptyEmail_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Email must be not empty!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithInvalidEmailFormatWithoutAtSig_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("testtest.com");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Invalid email format!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithInvalidEmailFormatTooMuchAtSign_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@@test.com");
        command.setPhone("+363456789");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Invalid email format!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidValidRegisterCommandWithEmptyPhone_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Phone must be not empty!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithInvalidPhoneFormatWithoutPlusSign_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("3612345678");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                                "Don't use spaces, hyphen or dashes!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithInvalidPhoneFormatWithSpace_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+36 123 45678");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                                "Don't use spaces, hyphen or dashes!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithInvalidPhoneFormatWithHyphen_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+36-123-45678");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                                "Don't use spaces, hyphen or dashes!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithInvalidPhoneFormatWithDash_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+36–123–45678");
        command.setPostalCode(1234);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Please provide a valid phone number in international format (e.g.: +1234567890)! " +
                                "Don't use spaces, hyphen or dashes!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithEmptyPostalCode_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+3612345678");
        command.setPostalCode(null);
        command.setCity("Test City");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Postal code must be not null!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithEmptyCity_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+3612345678");
        command.setPostalCode(1234);
        command.setCity("");
        command.setAddress("Test Address");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "City must be not empty!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void
    givenInvalidValidRegisterCommandWithEmptyAddress_whenRegister_thenReturnsBadRequestStatus()
            throws Exception {

        RegisterCommand command = new RegisterCommand();
        command.setName("Test Name");
        command.setPassword("Test2password");
        command.setEmail("test@test.com");
        command.setPhone("+3612345678");
        command.setPostalCode(1234);
        command.setCity("City");
        command.setAddress("");

        mockMvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Address must be not empty!")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenAnInvalidCustomerId_whenGetCustomerById_thenReturnNotFoundStatus() throws Exception {

        saveCustomer();

        mockMvc.perform(get("/api/customers/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException().getMessage().contains(
                        "Customer not found with this id: 6")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidCustomerId_whenGetCustomerById_thenReturnOkStatus() throws Exception {

        saveCustomer();

        mockMvc.perform(get("/api/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAValidCustomerId_whenDeleteCustomerById_thenReturnTheResponse() throws Exception {

        saveCustomer();

        mockMvc.perform(delete("/api/customers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(
                        "You deleted the customer with id: 1",
                        result.getResponse().getContentAsString()));
    }

//    @Test
//    public void givenAValidCustomerId_whenUpdateEmail_thenReturnTheResponseWithTheNewEmail() throws Exception {
//
//        saveCustomer();
//
//        mockMvc.perform(put("/api/customers/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"testy@email.com\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void givenAValidCustomerId_whenUpdatePhone_thenReturnTheResponseWithTheNewPhone() throws Exception {
//
//        saveCustomer();
//
//        mockMvc.perform(put("/api/customers/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"phone\":\"+36124356\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void givenAValidCustomerId_whenUpdateCity_thenReturnTheResponseWithTheNewCity() throws Exception {
//
//        saveCustomer();
//
//        mockMvc.perform(put("/api/customers/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"city\":\"New city\"}"))
//                .andExpect(status().isOk())
//                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("New city")));
//    }
//
//    @Test
//    public void givenAValidCustomerId_whenUpdatePostalCode_thenReturnTheResponseWithTheNewPostalCode() throws Exception {
//
//        saveCustomer();
//
//        mockMvc.perform(put("/api/customers/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"postalCode\":\"2600\"}"))
//                .andExpect(status().isOk())
//                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("2600")));
//    }
//
//    @Test
//    public void givenAValidCustomerId_whenUpdateAddress_thenReturnTheResponseWithTheNewAddress() throws Exception {
//
//        saveCustomer();
//
//        mockMvc.perform(put("/api/customers/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"address\":\"Test street 23\"}"))
//                .andExpect(status().isOk());
//    }

    private void saveCustomer() {
        entityManager.createNativeQuery("INSERT INTO customer" +
                        "(id, name, password, email, phone, postal_code, city, address)" +
                        "VALUES (1, 'Test Elek', 'test1Password', 'test@email.com','+36123456',1234," +
                        "'Test city','Test street 22')")
                .executeUpdate();
    }


}
