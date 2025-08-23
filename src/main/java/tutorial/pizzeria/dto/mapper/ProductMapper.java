package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Category;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.dto.outgoing.ProductListItem;

import java.util.List;

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
        product.setPrice(Double.valueOf(command.getPrice() + " Ft"));
        product.setIsAvailable(true);
        product.setCategory(category);

        return product;

    }

    public ProductListItem entitiesToDto(List<Product> products) {

        ProductListItem productListItem = new ProductListItem();

        productListItem.setName(products.stream()
                .map(Product::getName)
                .toList().toString());
        productListItem.setPrice(Double.valueOf(products.stream()
                .map(Product::getPrice)
                .toList().toString()));

        return productListItem;
    }
}
