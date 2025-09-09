package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductResponse {

    private List<ProductListItem> updatedProducts;

    private List<String> notFoundNames;

    private String message;
}
