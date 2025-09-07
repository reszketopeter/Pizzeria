package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Category;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.incoming.ProductModificationCommand;
import tutorial.pizzeria.dto.mapper.ProductMapper;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.dto.outgoing.ProductListItem;
import tutorial.pizzeria.exception.CategoryNotFoundException;
import tutorial.pizzeria.exception.ProductAlreadyExistException;
import tutorial.pizzeria.exception.ProductNotFoundException;
import tutorial.pizzeria.repository.CategoryRepository;
import tutorial.pizzeria.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    public ProductDetails createProduct(ProductCommand command) {
        if (productRepository.findByName(command.getName()).isPresent()) {
            throw new ProductAlreadyExistException
                    ("There is already a product with this name in the database!");
        }
        Category category = categoryRepository.findById(command.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException
                        ("Sorry, the category with this id" + command.getCategoryId() + "does not exist"));
        Product newProduct = productMapper.dtoToEntity(command, category);
//        newProduct.setCategory(category);
        productRepository.save(newProduct);
        return productMapper.entityToDto(newProduct);
    }

    public List<ProductListItem> createBulkProduct(List<ProductCommand> commands) {
        List<String> productNames = commands.stream()
                .map(ProductCommand::getName)
                .toList();

        if (productRepository.findByNames(productNames).isPresent()) {
            throw new ProductAlreadyExistException
                    ("There is already product(s) with this/these name(s) in the database! " + productNames);
        }
        List<Long> productCategoryId = commands.stream()
                .map(ProductCommand::getCategoryId)
                .distinct()
                .toList();

        List<Category> categories = categoryRepository.findByProductCategoryId(productCategoryId);
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException
                    ("Sorry, one or more categories with these IDs do not exist: " + productCategoryId);
        }
        List<Product> products = productMapper.dtoToEntities(commands, categories);
        List<Product> savedProducts = productRepository.saveAll(products);
        return productMapper.entitiesToDto(savedProducts);
    }

    public ProductDetails getProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("No product with this name in the database!"));
        return productMapper.entityToDto(product);
    }

    public List<ProductListItem> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Sorry, we didn't find any product in the database.");
        }
        return productMapper.entitiesToDto(products);
    }

    public ProductDetails changeProductDetails(String name, ProductModificationCommand command) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Sorry, we didn't find any product in the database"));
        product.setName(command.getName());
        product.setDescription(command.getDescription());
        product.setPrice(command.getPrice());
        productRepository.save(product);
        return productMapper.entityToDto(product);
    }

    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product with this id in the database!"));
        productRepository.delete(product);
    }
}
