package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.exceptions.ResourceWithIdNotFoundException;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
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
        CategoryEntity newCategory = oldCategory.get();
        newCategory.setName(ifFirstIsNullReturnSecond(category.getName(), newCategory.getName()));
        newCategory.setDescription(ifFirstIsNullReturnSecond(category.getDescription(), newCategory.getDescription()));

        categoriesRepository.save(newCategory);
    }

    private void throwNotFoundCategory(UUID id) {
        String error = "Category with ID: ".concat(id.toString()).concat(" Not Found");
        throw new ResourceWithIdNotFoundException(error);
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
