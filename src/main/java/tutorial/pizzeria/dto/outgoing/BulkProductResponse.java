package tutorial.pizzeria.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkProductResponse {

    private List<ProductListItem> savedProducts;

    private String message;

    private List<String> skippedNames;

}
