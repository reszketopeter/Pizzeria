package tutorial.pizzeria.dto.incoming;

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
    private String email;

    @NotBlank(message = "The email must be not empty!")
    private String password;
}
