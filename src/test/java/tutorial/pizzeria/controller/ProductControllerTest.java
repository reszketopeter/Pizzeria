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
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.incoming.ProductModificationCommand;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ProductControllerTest {

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
    void givenAValidProductCommand_whenCreateProduct_thenReturnTheResponseAndTheCreatedStatus() throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName("Hawaii pizza");
        command.setDescription("Pizza with tomato sauce, cheese, ham and pineapple");
        command.setPrice(3490);
        command.setCategoryID(1L);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Hawaii pizza")));
    }

    @Test
    void givenAnInvalidProductCommandWithEmptyName_whenCreateProduct_thenReturnUnprocessableEntityStatus()
            throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName(null);
        command.setDescription("Pizza with tomato sauce, cheese, ham and pineapple");
        command.setPrice(3490);
        command.setCategoryID(1L);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenAnInvalidProductCommandWithEmptyDescription_whenCreateProduct_thenReturnUnprocessableEntityStatus()
            throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName("Hawaii pizza");
        command.setDescription(null);
        command.setPrice(3490);
        command.setCategoryID(1L);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenAnInvalidProductCommandWithEmptyPrice_whenCreateProduct_thenReturnUnprocessableEntityStatus()
            throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName("Hawaii pizza");
        command.setDescription("Pizza with tomato sauce, cheese, ham and pineapple");
        command.setPrice(null);
        command.setCategoryID(1L);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenAnInvalidProductCommandWithNegativePrice_whenCreateProduct_thenReturnUnprocessableEntityStatus()
            throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName("Hawaii pizza");
        command.setDescription("Pizza with tomato sauce, cheese, ham and pineapple");
        command.setPrice(-3690);
        command.setCategoryID(1L);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenAnInvalidProductCommandWithEmptyCategoryID_whenCreateProduct_thenReturnUnprocessableEntityStatus()
            throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName("Hawaii pizza");
        command.setDescription("Pizza with tomato sauce, cheese, ham and pineapple");
        command.setPrice(3690);
        command.setCategoryID(null);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenAnInvalidProductCommandWithNonExistingCategoryID_whenCreateProduct_thenReturnUnprocessableEntityStatus()
            throws Exception {

        saveCategory();

        ProductCommand command = new ProductCommand();
        command.setName("Hawaii pizza");
        command.setDescription("Pizza with tomato sauce, cheese, ham and pineapple");
        command.setPrice(3690);
        command.setCategoryID(2L);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnExistingProduct_whenGetProductByName_thenReturnProductDetails() throws Exception {

        saveCategory();
        saveProduct();

        mockMvc.perform(get("/api/products/{name}", "Hawaii pizza")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Pizza with tomato sauce")));
    }

    @Test
    void givenANonExistingProduct_whenGetProductByName_thenReturnNotFoundStatus() throws Exception {

        mockMvc.perform(get("/api/products/{name}", "Margarita pizza")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenTwoExistingProduct_whenGetAll_thenReturnProductListItemAndOkStatus() throws Exception {

        saveCategory();
        saveProduct();
        saveAnotherProduct();

        mockMvc.perform(get("/api/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenNoProductInTheDatabase_whenGetAll_thenReturnNotFoundStatusAndResponse() throws Exception {

        saveCategory();

        mockMvc.perform(get("/api/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers
                        .containsString("Sorry, we didn't find any product in the database.")));
    }

    @Test
    void givenAnExistingProduct_whenChangeProductDetails_thenReturnTheNewProductDetailsAndOkStatus() throws Exception {

        saveCategory();
        saveProduct();

        ProductModificationCommand command = new ProductModificationCommand();
        command.setName("Hawaii pizza");
        command.setDescription("Pizza with tomato sauce, cheese, ham, basil and pineapple");
        command.setPrice(3490);

        mockMvc.perform(put("/api/products/{name}", "Hawaii pizza")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers
                        .containsString("Pizza with tomato sauce, cheese, ham, basil and pineapple")));
    }

    @Test
    void givenAnExistingProduct_whenDeleteProductById_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCategory();
        saveProduct();

        mockMvc.perform(delete("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("You have successfully deleted a product.")));
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
                        "(id,name,description,price,category_id)" +
                        "VALUES (2, 'Ham pizza', 'Pizza with tomato sauce, ham and cheese', 3390, 1)")
                .executeUpdate();
    }
}
