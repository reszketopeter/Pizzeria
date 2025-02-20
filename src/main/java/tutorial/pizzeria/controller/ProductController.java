package tutorial.pizzeria.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.dto.outgoing.ProductListItem;
import tutorial.pizzeria.service.ProductService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDetails> createProduct(@RequestBody ProductCommand command) {
        log.info("Create New Product: {}", command);
        ProductDetails response = productService.createProduct(command);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductDetails> getProductByName(@PathVariable String name) {
        log.info("Get Product By Name: {}", name);
        ProductDetails response = productService.getProductByName(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ProductListItem> getAllProducts() {
        log.info("Get All Products");
        ProductListItem response = productService.getAllProducts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
