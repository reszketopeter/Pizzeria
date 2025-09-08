package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Category;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.dto.outgoing.ProductListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductMapper {


    public ProductDetails entityToDto(Product product) {

        ProductDetails productDetails = new ProductDetails();

        productDetails.setProductId(product.getId());
        productDetails.setName(product.getName());
        productDetails.setDescription(product.getDescription());
        productDetails.setPrice(product.getPrice());
        productDetails.setCategoryName(product.getCategory().getName());

        return productDetails;
    }

    public Product dtoToEntity(ProductCommand command, Category category) {

        Product product = new Product();

        product.setName(command.getName());
        product.setDescription(command.getDescription());
        product.setPrice(command.getPrice());
        product.setIsAvailable(true);
        product.setCategory(category);

        return product;

    }

    public List<ProductListItem> entitiesToDto(List<Product> products) {

        return products.stream()
                .map(product -> {
                    ProductListItem dto = new ProductListItem();
                    dto.setProductId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    return dto;
                })
                .toList();
    }

    public List<Product> dtoToEntities(List<ProductCommand> validCommands, List<Category> categories) {

        Map<Long, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        List<Product> products = new ArrayList<>();

        for (ProductCommand command : validCommands) {

            Category category = categoryMap.get(command.getCategoryId());

            Product product = new Product();

            product.setName(command.getName());
            product.setPrice(command.getPrice());
            product.setDescription(command.getDescription());
            product.setIsAvailable(true);
            product.setCategory(category);

            products.add(product);
        }

        return products;
    }
}
