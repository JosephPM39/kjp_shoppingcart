package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.services.CategoryService;
import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoriesService;

    @Autowired
    public CategoryController(CategoryService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public List<CategoryEntity> getAll() {
        return categoriesService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryEntity getById(@PathVariable UUID id) {
        return categoriesService.getById(id);
    }

    @PostMapping("/{id}/products")
    public void addProductsToCategory(@PathVariable UUID id, @RequestBody ProductsIdListDTO productsId) {
        categoriesService.addProductsToCategory(id, productsId);
    }

    @DeleteMapping("/{id}/products/{productId}")
    public void removeProductFromCategory(@PathVariable UUID id, @PathVariable UUID productId) {
        categoriesService.removeProductFromCategory(id, productId);
    }

    @PostMapping
    public void create(@Validated(CreateGroup.class) @RequestBody CategoryEntity category) {
        categoriesService.create(category);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable UUID id, @Validated(UpdateGroup.class) @RequestBody CategoryEntity category) {
        categoriesService.update(id, category);
    }

    @PostMapping("/{id}/disable")
    public void disable(@PathVariable UUID id) {
        categoriesService.disable(id);
    }

    @PostMapping("/{id}/enable")
    public void enable(@PathVariable UUID id) {
        categoriesService.enable(id);
    }

}
