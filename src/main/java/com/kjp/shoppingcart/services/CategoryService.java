package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.AddProductsToCategoryDTO;
import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import com.kjp.shoppingcart.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    private final ICategoryRepository categoriesRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<CategoryEntity> getAll() {
        return categoriesRepository.findAll();
    }

    public CategoryEntity getById(UUID id) {
        Optional<CategoryEntity> category = categoriesRepository.findById(id);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category with ID: ".concat(id.toString()).concat(" Not Found"));
        }
        return category.get();
    }

    public void create(CategoryEntity category) {
        categoriesRepository.save(category);
    }

    public void remove(UUID id) {
        categoriesRepository.deleteById(id);
    }

    public void update(UUID id, CategoryEntity category) {
        CategoryEntity oldCategory = this.getById(id);
        CategoryEntity categoryWithChanges = ObjectUtils.getInstanceWithNotNullFields(category, oldCategory, CategoryEntity.class);
        categoriesRepository.save(categoryWithChanges);
    }

    public void disable(UUID id) {
        CategoryEntity changes = new CategoryEntity();
        changes.setDisabled(true);
        this.update(id, changes);
    }

    public void enable(UUID id) {
        CategoryEntity changes = new CategoryEntity();
        changes.setDisabled(false);
        this.update(id, changes);
    }

    public void addProductsToCategory(UUID id, AddProductsToCategoryDTO productsId) {
        List<ProductEntity> productEntities = new ArrayList<>();
        for(UUID productId : productsId.productsId()) {
            ProductEntity product = new ProductEntity();
            product.setId(productId);
            productEntities.add(product);
        }
        CategoryEntity category = this.getById(id);
        category.getProducts().addAll(productEntities);
        this.categoriesRepository.save(category);
    }


}
