package tutorial.pizzeria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.CategoryCommand;
import tutorial.pizzeria.dto.mapper.CategoryMapper;
import tutorial.pizzeria.dto.outgoing.CategoryDetails;
import tutorial.pizzeria.service.CategoryService;

@Tag(name = "Category API", description = "Operations related to category creation, retrieval, update and deletion")
@RestController
@Slf4j
@RequestMapping("/api/categories")
public class CategoryController {

        private final CategoryService categoryService;
        private final CategoryMapper categoryMapper;

        public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
                this.categoryService = categoryService;
                this.categoryMapper = categoryMapper;
        }

        @Operation(summary = "Create new category", description = "Creates a new category based on the provided data.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Category successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/create")
        public ResponseEntity<String> create(@Valid @RequestBody CategoryCommand command) {
                log.info("Create New Category");
                categoryService.create(command);
                return new ResponseEntity<>("You have successfully created a new category!", HttpStatus.CREATED);
        }

        @Operation(summary = "Get category by name", description = "Retrieves category details based on the category name.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Category found"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/{name}")
        public ResponseEntity<CategoryDetails> getCategory(@PathVariable String name) {
                log.info("Get Category By Name: {}", name);
                CategoryDetails response = categoryService.getCategory(name);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Update category", description = "Updates category details based on the provided ID and data.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Category successfully updated"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PutMapping("{id}")
        public ResponseEntity<CategoryDetails> changeCategory(@PathVariable Long id,
                        @Valid @RequestBody CategoryCommand command) {
                log.info("Change Categorydetails with id: {}", id);
                CategoryDetails response = categoryService.changeCategory(id, command);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Delete category by name", description = "Deletes a category based on its name.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @DeleteMapping("/{name}")
        public ResponseEntity<String> deleteCategory(@PathVariable String name) {
                log.info("Delete Category With Name: {}", name);
                categoryService.deleteCategory(name);
                return new ResponseEntity<>("You successfully deleted the category!", HttpStatus.OK);
        }
}
