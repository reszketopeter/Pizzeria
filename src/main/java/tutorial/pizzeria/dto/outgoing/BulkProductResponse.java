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

    private List<String> skippedNames;

    private String message;

    private String updateEndpoint;


}
