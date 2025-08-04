package tutorial.pizzeria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.dto.incoming.OrderCommand;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    private ObjectMapper objectMapper;

    private MockHttpServletRequest request;

    private HttpSession session;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        request = new MockHttpServletRequest();
        session = request.getSession();
    }

    // The problem is in the OrderService. The Order is created earlier, than the OrderItem is added to the Order.
    @Test
    void givenAnExistingProductId_whenCreateNewOrder_thenReturnTheResponseAndCreatedStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("customerId", 1L);

        OrderCommand command = new OrderCommand();
        command.setQuantity(1);


        mockMvc.perform(post("/api/orders/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .session(session))
                .andExpect(status().isCreated());
    }

    @Test
    void givenAnAlreadyExistingOrder_whenCreateOrder_thenFindThePreviousOrder() throws Exception {

        saveOrder();
        saveCategory();
        saveProduct();
    }

    @Test
    void givenANonExistingCustomerId_whenCreateNewOrder_thenReturnExceptionAndNoContentStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        OrderCommand command = new OrderCommand();
        command.setQuantity(1);

        mockMvc.perform(post("/api/orders/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("The customer id is null. First of all you have to login!")));
    }

    @Test
    void givenAWrongCustomerId_whenCreateNewOrder_thenReturnExceptionAndNoContentStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        OrderCommand command = new OrderCommand();
        command.setQuantity(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("customerId", 2L);


        mockMvc.perform(post("/api/orders/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("The customer id is null. First of all you have to login!")));
    }

    @Test
    void givenANonExistingProductId_whenCreateNewOrder_thenReturnExceptionAndNotFoundStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        OrderCommand command = new OrderCommand();
        command.setQuantity(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("customerId", 1L);

        mockMvc.perform(post("/api/orders/{productId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("No product with this id in the database!")));
    }

    @Test
    void givenAnExistingProductThatIsNotAvailable_whenCreateNewOrder_thenReturnExceptionAndConflictStatus()
            throws Exception {

        saveCustomer();
        saveCategory();
        saveAnotherProduct();

        OrderCommand command = new OrderCommand();
        command.setQuantity(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("customerId", 1L);

        mockMvc.perform(post("/api/orders/{productId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .session(session))
                .andExpect(status().isConflict())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("Apologise, but this product is not available now.")));
    }

    @Test
    void givenAnExistingOrderId_whenDeleteOrderById_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveOrder();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("customerId", 1L);

        mockMvc.perform(delete("/api/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk());

    }

    @Test
    void givenANonExistingOrderId_whenDeleteOrderById_thenReturnTheResponseAndNotFoundStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveOrder();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("customerId", 1L);

        mockMvc.perform(delete("/api/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("Sorry, there is not any Order with this id: 2")));

    }


    private void saveOrder() {

        entityManager.createNativeQuery("INSERT INTO purchase" +
                        "(id, time_stamp, total_price, city_of_order, customer_id, order_status)" +
                        "VALUES (1,'2025-07-15', 2490, 'Budapest', 1, 'PENDING')")
                .executeUpdate();
    }

    private void saveCategory() {

        entityManager.createNativeQuery("INSERT INTO category" +
                        "(id, name, description)" +
                        "VALUES (1, 'Pizza with tomato sauce', 'Pizza with tomato sauce')")
                .executeUpdate();
    }

    private void saveProduct() {

        entityManager.createNativeQuery("INSERT INTO product" +
                        "(id, name, description, price, category_id)" +
                        "VALUES  (1, 'Hawaii pizza', 'Pizza with tomato sauce, cheese, ham and pineapple', 3490, 1)")
                .executeUpdate();
    }

    private void saveAnotherProduct() {

        entityManager.createNativeQuery("INSERT INTO product" +
                        "(id, name, description, price, category_id, available)" +
                        "VALUES  (2, 'Vegetarian pizza', 'Pizza with tomato sauce, broccoli and cheese', 3290, 1, false)")
                .executeUpdate();
    }

    private void saveCustomer() {
        entityManager.createNativeQuery("INSERT INTO customer" +
                        "(id, name, password, email, phone, postal_code, city, address, role)" +
                        "VALUES (1, 'Test Elek', 'test1Password', 'test@email.com','+36123456',1234," +
                        "'Test city','Test street 22', 'GUEST')")
                .executeUpdate();
    }

}
