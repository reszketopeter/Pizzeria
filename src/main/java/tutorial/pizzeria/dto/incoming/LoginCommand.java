package tutorial.pizzeria.dto.incoming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginCommand {

    @NotBlank(message = "The email must be not empty!")
    @Schema(description = "The email with which the customer registered in the system.",
            example = "test.peter@gmail.com")
    private String email;

    @NotBlank(message = "The email must be not empty!")
    @Schema(description = "The password with which the customer registered in the system.",
            example = "TestPassword1")
    private String password;
}
