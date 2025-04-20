package tutorial.pizzeria.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.CategoryCommand;
import tutorial.pizzeria.dto.mapper.CategoryMapper;
import tutorial.pizzeria.dto.outgoing.CategoryDetails;
import tutorial.pizzeria.service.CategoryService;

@RestController
@Slf4j
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody CategoryCommand command) {
        log.info("Create New Category");
        categoryService.create(command);
        return new ResponseEntity<>("You have successfully created a new category", HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryDetails> getCategory(@PathVariable String name) {
        log.info("Get Category By Name: {}", name);
        CategoryDetails response = categoryService.getCategory(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDetails> changeCategory(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryCommand command) {
        log.info("Change Categorydetails with id: {}", id);
        CategoryDetails response = categoryService.changeCategory(id, command);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable String name) {
        log.info("Delete Category With Name: {}", name);
        categoryService.deleteCategory(name);
        return new ResponseEntity<>("You successfully deleted the category!", HttpStatus.OK);
    }
}
