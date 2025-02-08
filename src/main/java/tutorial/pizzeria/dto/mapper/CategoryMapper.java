package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Category;
import tutorial.pizzeria.dto.incoming.CategoryCommand;
import tutorial.pizzeria.dto.outgoing.CategoryDetails;

@Component
public class CategoryMapper {

    public Category dtoToEntity(CategoryCommand command) {

        Category category = new Category();

        category.setName(command.getName());
        category.setDescription(command.getDescription());

        return category;
    }

    public CategoryDetails entityToDto(Category newCategory) {

        CategoryDetails categoryDetails = new CategoryDetails();

        categoryDetails.setName(newCategory.getName());
        categoryDetails.setDescription(newCategory.getDescription());

        return categoryDetails;
    }
}
