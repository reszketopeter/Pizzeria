package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.dto.incoming.ProductCommand;
import tutorial.pizzeria.dto.outgoing.ProductDetails;
import tutorial.pizzeria.exception.ProductAlreadyExistException;
import tutorial.pizzeria.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//    public ProductDetails createProduct(ProductCommand command) {
//        productRepository.findByName(command.getName())
//                .orElseThrow(() -> new ProductAlreadyExistException
//                        ("There is already a product with this name in the database!"));
//
//
//    }
}
