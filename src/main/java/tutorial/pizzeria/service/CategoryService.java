package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Category;
import tutorial.pizzeria.dto.incoming.CategoryCommand;
import tutorial.pizzeria.dto.mapper.CategoryMapper;
import tutorial.pizzeria.dto.outgoing.CategoryDetails;
import tutorial.pizzeria.exception.CategoryAlreadyExistsException;
import tutorial.pizzeria.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDetails create(CategoryCommand command) {
        categoryRepository.findByName(command.getName())
                .orElseThrow(() -> new CategoryAlreadyExistsException("There is a category with this name in the system"));

        Category newCategory = categoryMapper.dtoToEntity(command);
        categoryRepository.save(newCategory);
        return categoryMapper.entityToDto(newCategory);
    }

    public CategoryDetails getCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryAlreadyExistsException("There is a category with this name in the system"));
        return categoryMapper.entityToDto(category);
    }
}
