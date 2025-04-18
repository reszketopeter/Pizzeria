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
import tutorial.pizzeria.dto.incoming.CategoryCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryControllerTest {

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
    void givenNewCategoryCommand_whenCreate_thenReturnNewCategoryAndOkStatus() throws Exception {

        CategoryCommand command = new CategoryCommand();
        command.setName("Pizza");
        command.setDescription("Delicious pizza");

        mockMvc.perform(post("/api/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(result -> assertEquals("You have successfully created a new category",
                        result.getResponse().getContentAsString()))
                .andExpect(status().isCreated());
    }

    @Test
    void givenNewCategoryCommandWithAnEmptyName_whenCreate_thenReturnUnprocessableEntityException() throws Exception {

        CategoryCommand command = new CategoryCommand();
        command.setName("");
        command.setDescription("Delicious pizza");

        mockMvc.perform(post("/api/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenNewCategoryCommandWithEmptyDescription_whenCreate_thenReturnUnprocessableEntityException() throws Exception {

        CategoryCommand command = new CategoryCommand();
        command.setName("Pizza");
        command.setDescription("");

        mockMvc.perform(post("/api/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenNewCategoryCommandWithAnAlreadyExistCategory_whenCreate_thenReturnConflictException()
            throws Exception {

        saveCategory();

        CategoryCommand command = new CategoryCommand();
        command.setName("Pizza");
        command.setDescription("Delicious pizza");

        mockMvc.perform(post("/api/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isConflict());
    }

    @Test
    void givenAnExistingCategoryName_whenGetCategory_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCategory();

        mockMvc.perform(get("/api/category/{name}", "Pizza")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("delicious pizza"));
    }

    @Test
    void givenANonExistingCategoryName_whenGetCategory_thenReturnNotFoundStatus() throws Exception {

        mockMvc.perform(get("/api/category/{name}", "Pizza")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnExistingCategoryIdAndCategoryCommand_whenChangeCategory_thenReturnTheResponseAndOkStatus()
            throws Exception {

        saveCategory();

        CategoryCommand command = new CategoryCommand();
        command.setName("Pizza");
        command.setDescription("Very delicious pizza");

        mockMvc.perform(put("/api/category/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Very delicious pizza")));
//                .andExpect(result ->
//                        result.getResponse().getContentAsString().contains("Very delicious pizza"));
    }

    @Test
    void givenANonExistingCategoryIdAndCategoryCommand_whenChangeCategory_thenReturnNotFoundStatus()
            throws Exception {

        CategoryCommand command = new CategoryCommand();
        command.setName("Pizza");
        command.setDescription("Very delicious pizza");

        mockMvc.perform(put("/api/category/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAnExistingCategoryName_whenDeleteCategory_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCategory();

        mockMvc.perform(delete("/api/category/{name}", "Pizza")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString
                        ("You successfully deleted the category!")));
//                .andExpect(result -> result.getResponse().getContentAsString()
//                        .contains("You successfully deleted the category!"));
    }


    private void saveCategory() {

        entityManager.createNativeQuery("INSERT INTO category (id, name, description) VALUES " +
                        "(1, 'Pizza','delicious pizza')")
                .executeUpdate();

    }
}

