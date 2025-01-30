package tutorial.pizzeria.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginCommand {

    @NotBlank(message = "The email must be not empty!")
    private String email;

    @NotBlank(message = "The email must be not empty!")
    private String password;
}
