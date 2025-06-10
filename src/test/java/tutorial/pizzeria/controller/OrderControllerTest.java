package tutorial.pizzeria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.dto.incoming.OrderCommand;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@AutoConfigureMockMvc
public class OrderControllerTest {

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
    void givenAnExistingProductId_whenCreateNewOrder_thenReturnTheResponseAndCreatedStatus() throws Exception {

        // The customer should log in.
        saveCustomer();
        saveCategory();
        saveProduct();

        OrderCommand command = new OrderCommand();
        command.setQuantity(1);

        mockMvc.perform(post("/api/orders/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                        .sessionAttr("customerId", 1L))
                .andExpect(status().isCreated());
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

    private void saveCustomer() {
        entityManager.createNativeQuery("INSERT INTO customer" +
                        "(id, name, password, email, phone, postal_code, city, address, role)" +
                        "VALUES (1, 'Test Elek', 'test1Password', 'test@email.com','+36123456',1234," +
                        "'Test city','Test street 22', 'GUEST')")
                .executeUpdate();
    }

}
