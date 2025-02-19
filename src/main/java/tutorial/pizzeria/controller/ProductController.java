package tutorial.pizzeria.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorial.pizzeria.domain.Product;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.service.ProductService;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @PostMapping
//    public ResponseEntity<ProductDetails> createProduct(@RequestBody ProductCommand command) {
//        log.info("Create New Product: {}", command);
//        productService.createProduct(command);
//    }
}
