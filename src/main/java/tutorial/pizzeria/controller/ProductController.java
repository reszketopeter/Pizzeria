package tutorial.pizzeria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.incoming.ProductModificationCommand;
import tutorial.pizzeria.dto.outgoing.BulkProductResponse;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.dto.outgoing.ProductListItem;
import tutorial.pizzeria.dto.outgoing.UpdateProductResponse;
import tutorial.pizzeria.service.ProductService;

import java.util.List;

@Tag(name = "Product API", description = "Operations related to category creation, retrieval, update and deletion")
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create new product",
            description = "Creates a new product based on the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<ProductDetails> createProduct(@Valid @RequestBody ProductCommand command) {
        log.info("Create New Product: {}", command);
        ProductDetails response = productService.createProduct(command);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create new products",
            description = "Creates new products based on the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Products successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create/bulk")
    public ResponseEntity<BulkProductResponse> createProducts(@Valid @RequestBody List<ProductCommand> commands) {
        log.info("Create New Products: {}", commands);
        BulkProductResponse response = productService.createBulkProduct(commands);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get product by name",
            description = "Retrieves product details based on the category name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{name}")
    public ResponseEntity<ProductDetails> getProductByName(@PathVariable String name) {
        log.info("Get Product By Name: {}", name);
        ProductDetails response = productService.getProductByName(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all products",
            description = "Returns a list of all registered products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProductListItem>> getAllProducts() {
        log.info("Get All Products");
        List<ProductListItem> response = productService.getAllProducts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Update product",
            description = "Updates product details based on the provided name and data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{name}")
    public ResponseEntity<ProductDetails> changeProductDetails(@PathVariable String name,
                                                               @Valid @RequestBody ProductModificationCommand command) {
        log.info("Change ProductDetails By Name: {}", name);
        ProductDetails response = productService.changeProductDetails(name, command);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Update products",
            description = "Updates products details based on the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/updates")
    public ResponseEntity<UpdateProductResponse> updateProducts
            (@Valid @RequestBody List<ProductModificationCommand> commands) {
        log.info("Change ProductDetails By Data");
        UpdateProductResponse updateProductResponse = productService.updateProducts(commands);
        return new ResponseEntity<>(updateProductResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product by ID",
            description = "Deletes a product from the system based on their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        log.info("Delete Product By Id: {}", id);
        productService.deleteProductById(id);
        return new ResponseEntity<>("You have successfully deleted a product.", HttpStatus.OK);
    }

    // Image uploading
    /*
    1. User uploads an image through the frontend.
    2. The backend receives the image and transfers it to the file storage server instead of keeping it locally.
    3. The backend stores only the file path or URL in the database.
    4. When needed, the application retrieves the image from the file storage.*/
}
