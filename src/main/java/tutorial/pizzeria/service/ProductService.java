package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Category;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.incoming.ProductModificationCommand;
import tutorial.pizzeria.dto.mapper.ProductMapper;
import tutorial.pizzeria.dto.outgoing.BulkProductResponse;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.dto.outgoing.ProductListItem;
import tutorial.pizzeria.dto.outgoing.UpdateProductResponse;
import tutorial.pizzeria.exception.CategoryNotFoundException;
import tutorial.pizzeria.exception.ProductAlreadyExistException;
import tutorial.pizzeria.exception.ProductNotFoundException;
import tutorial.pizzeria.repository.CategoryRepository;
import tutorial.pizzeria.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        productRepository.save(newProduct);
        return productMapper.entityToDto(newProduct);
    }

    public BulkProductResponse createBulkProduct(List<ProductCommand> commands) {
        List<String> productNames = commands.stream()
                .map(ProductCommand::getName)
                .toList();

        List<String> alreadyExistingProductsNames = productRepository.findByNames(productNames);

        List<ProductCommand> validCommands = commands.stream()
                .filter(cmd -> !alreadyExistingProductsNames.contains(cmd.getName()))
                .toList();

        List<Long> productCategoryId = validCommands.stream()
                .map(ProductCommand::getCategoryId)
                .distinct()
                .toList();

        List<Category> categories = categoryRepository.findByProductCategoryId(productCategoryId);
        List<Long> categoryIds = categories.stream()
                .map(Category::getId)
                .distinct()
                .toList();

        Set<Long> requestedIds = new HashSet<>(productCategoryId);
        Set<Long> existingIds = new HashSet<>(categoryIds);

        requestedIds.removeAll(existingIds);

        if (!requestedIds.isEmpty()) {
            throw new CategoryNotFoundException(
                    "Sorry, one or more categories with these IDs do not exist: " + requestedIds
            );
        }
        List<Product> products = productMapper.dtoToEntities(validCommands, categories);
        List<Product> savedProducts = productRepository.saveAll(products);
        List<ProductListItem> productsDto = productMapper.entitiesToDto(savedProducts);

        String message = alreadyExistingProductsNames.isEmpty()
                ? "All products were saved successfully."
                : "Some product names are already in the database. You can update them using the bulk update endpoint.";

        return new BulkProductResponse(productsDto, alreadyExistingProductsNames, message,
                "/api/products/updates");
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
        Product updatedProduct = productMapper.changeEntity(product, command);
        productRepository.save(updatedProduct);
        return productMapper.entityToDto(updatedProduct);
    }

    public UpdateProductResponse updateProducts(List<ProductModificationCommand> commands) {
        List<String> productNames = commands.stream()
                .map(ProductModificationCommand::getOriginalName)
                .toList();

        List<String> existingNames = productRepository.findByNames(productNames);
        List<String> notFoundNames = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (ProductModificationCommand command : commands) {
            if (existingNames.contains(command.getOriginalName())) {
                Product product = productRepository.findByName(command.getOriginalName())
                        .orElseThrow(() ->
                                new ProductNotFoundException("Sorry, we didn't find this product in the database: " +
                                        command.getOriginalName()));
                Product updatedProduct = productMapper.changeEntity(product, command);
                productsToUpdate.add(updatedProduct);
            } else {
                notFoundNames.add(command.getOriginalName());
            }
        }

        List<Product> savedProducts = productRepository.saveAll(productsToUpdate);
        List<ProductListItem> productsDto = productMapper.entitiesToDto(savedProducts);

        String message = notFoundNames.isEmpty()
                ? "All products were updated successfully."
                : "Some products were updated. The following names were not found: " + notFoundNames;

        return new UpdateProductResponse(productsDto, notFoundNames, message);
    }

    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No product with this id in the database!"));
        productRepository.delete(product);
    }
}
