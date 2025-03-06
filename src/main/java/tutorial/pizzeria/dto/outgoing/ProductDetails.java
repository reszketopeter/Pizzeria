package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tutorial.pizzeria.domain.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {

    private Long productId;

    private String name;

    private String description;

    private Integer price;

    private Category category;
}
