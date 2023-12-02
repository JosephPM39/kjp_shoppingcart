package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import com.kjp.shoppingcart.specifications.EntitySpecification;
import com.kjp.shoppingcart.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriesService {
    private final ICategoryRepository categoriesRepository;

    @Autowired
    public CategoriesService(ICategoryRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<CategoryEntity> getAll() {
        return categoriesRepository.findAll();
    }

    public CategoryEntity getById(UUID id) {
        Optional<CategoryEntity> category = categoriesRepository.findById(id);
        if (!category.isPresent()) {
            throwNotFoundCategory(id);
        }
        return category.get();
    }

    public CategoryEntity getByName(String name) {
        Optional<CategoryEntity> category = categoriesRepository.findOne(EntitySpecification.fieldEquals("name", name));
        if (category.isPresent()) {
            return category.get();
        }
        String error = "Category with the \"name\": ".concat(name).concat(" not found");
        throw new ResourceNotFoundException(error);
    }

    public void create(CategoryEntity category) {
        categoriesRepository.save(category);
    }

    public void remove(UUID id) {
        throwErrorIfCategoryNotExistsById(id);
        categoriesRepository.deleteById(id);
    }

    public void update(UUID id, CategoryEntity category) {
        Optional<CategoryEntity> oldCategory = categoriesRepository.findById(id);
        if (!oldCategory.isPresent()) {
            throwNotFoundCategory(id);
        }

        CategoryEntity categoryWithChanges = ObjectUtils.copiarPropiedades(category, oldCategory.get(), CategoryEntity.class);

        categoriesRepository.save(categoryWithChanges);
    }

    private void throwNotFoundCategory(UUID id) {
        String error = "Category with ID: ".concat(id.toString()).concat(" Not Found");
        throw new ResourceNotFoundException(error);
    }

    private void throwErrorIfCategoryNotExistsById(UUID id) {
        Optional<CategoryEntity> category = categoriesRepository.findById(id);
        if (category.isPresent()) {
           return;
        }
        throwNotFoundCategory(id);
    }

    private <T extends Object> T ifFirstIsNullReturnSecond(T first, T second) {
        if (first == null) {
            return second;
        }
        return first;
    }
}
