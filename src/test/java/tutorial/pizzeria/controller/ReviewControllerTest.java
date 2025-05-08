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
import tutorial.pizzeria.domain.Customer;
import tutorial.pizzeria.domain.Recommendation;
import tutorial.pizzeria.dto.incoming.ReviewCommand;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@AutoConfigureMockMvc
public class ReviewControllerTest {

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
    public void givenAValidReviewCommand_whenCreateReview_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        Customer customer = (Customer) entityManager.createQuery("SELECT c FROM Customer c WHERE c.id = 1")
                .getSingleResult();

        ReviewCommand command = new ReviewCommand();
        command.setCustomer(customer);
        command.setContent("Very delicious pizza");
        command.setIsRecommend(Recommendation.YES);

        mockMvc.perform(post("/api/reviews/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("Very delicious pizza")));
    }

    @Test
    void givenInvalidReviewCommand_whenCreateReview_thenReturnUnprocessableEntityStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        Customer customer = (Customer) entityManager.createQuery("SELECT c FROM Customer c WHERE c.id = 1")
                .getSingleResult();

        ReviewCommand command = new ReviewCommand();
        command.setCustomer(customer);
        command.setContent("Very delicious pizza");
        command.setIsRecommend(null);

        mockMvc.perform(post("/api/reviews/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void givenAValidReviewCommandAndANonExistingProductId_whenCreateReview_thenReturnNotFoundStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();

        Customer customer = (Customer) entityManager.createQuery("SELECT c FROM Customer c WHERE c.id = 1")
                .getSingleResult();

        ReviewCommand command = new ReviewCommand();
        command.setCustomer(customer);
        command.setContent("Very delicious pizza");
        command.setIsRecommend(Recommendation.YES);

        mockMvc.perform(post("/api/reviews/{productId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenReviewsInTheDatabase_whenGetReviewsByRecommendStatus_thenReturnReviewListItemAndOkStatus()
            throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveReview();
        saveAnotherReview();

        mockMvc.perform(get("/api/reviews/{isRecommend}", Recommendation.YES.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    assertTrue(responseContent.contains("very good"));
                    assertTrue(responseContent.contains("2025-04-23"));
                });
    }

    @Test
    void givenJustNonRecommendedReviewsInTheDatabase_whenGetReviewsByRecommendStatus_thenReturnTheResponseAndNotFoundStatus()
            throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveReview();
        saveAnotherReview();

        mockMvc.perform(get("/api/reviews/{isRecommend}", Recommendation.NO.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers
                        .containsString("Not found any review with this condition: NO")));
    }

    // How should I test the chronological order?
    @Test
    void givenMoreReviews_whenGetReviewsByChronologicalDesc_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveReview();
        saveAnotherReview();

        mockMvc.perform(get("/api/reviews/chronological")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAnExistingReviewId_whenDeleteReview_thenReturnTheResponseAndOkStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveReview();

        mockMvc.perform(delete("/api/reviews/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("You successfully deleted the review!")));
    }

    @Test
    void givenANonExistingReviewId_whenDeleteReview_thenReturnTheResponseAndNotFoundStatus() throws Exception {

        saveCustomer();
        saveCategory();
        saveProduct();
        saveReview();
        saveAnotherReview();

        mockMvc.perform(delete("/api/reviews/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.
                        containsString("Sorry, the review with this id " + 3 + " does not exist.")));
    }


    private void saveCustomer() {
        entityManager.createNativeQuery("INSERT INTO customer" +
                        "(id, name, password, email, phone, postal_code, city, address, role)" +
                        "VALUES (1, 'Test Elek', 'test1Password', 'test@email.com','+36123456',1234," +
                        "'Test city','Test street 22', 'GUEST')")
                .executeUpdate();
    }

    private void saveProduct() {

        entityManager.createNativeQuery("INSERT INTO product" +
                        "(id, name, description, price, category_id)" +
                        "VALUES  (1, 'Hawaii pizza', 'Pizza with tomato sauce, cheese, ham and pineapple', 3490, 1)")
                .executeUpdate();
    }

    private void saveCategory() {

        entityManager.createNativeQuery("INSERT INTO category" +
                        "(id, name, description)" +
                        "VALUES (1, 'Pizza with tomato sauce', 'Pizza with tomato sauce')")
                .executeUpdate();
    }

    private void saveReview() {

        entityManager.createNativeQuery("INSERT INTO review" +
                        "(id,content,is_recommend,timestamp,customer_id)" +
                        "VALUES (1, 'very good','Yes','2025-04-27 12:00:00',1)")
                .executeUpdate();
    }

    private void saveAnotherReview() {

        entityManager.createNativeQuery("INSERT INTO review" +
                        "(id,content,is_recommend,timestamp,customer_id)" +
                        "VALUES (2, 'good','Yes','2025-04-23 15:32:05',1)")
                .executeUpdate();
    }

}
