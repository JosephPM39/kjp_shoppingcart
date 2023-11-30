package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.services.CategoriesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {
    private final CategoriesService categoriesService;

    @Autowired
    public CategoryController(CategoriesService categoriesService) {
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
    public void addProducts(@PathVariable UUID id, @RequestBody Object productsId) {

    }

    @PostMapping
    public void create(@Valid @RequestBody CategoryEntity category) {
        categoriesService.create(category);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable UUID id, @RequestBody CategoryEntity category) {
        categoriesService.update(id, category);
    }

    @PostMapping("/{id}")
    public void disable(@PathVariable UUID id) {

    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable UUID id) {
        categoriesService.remove(id);
    }
}