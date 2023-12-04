package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.services.ICategoryService;
import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
  private final ICategoryService categoriesService;

  @Autowired
  public CategoryController(ICategoryService categoriesService) {
    this.categoriesService = categoriesService;
  }

  @GetMapping
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public List<CategoryEntity> getAll() {
    return categoriesService.getAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public CategoryEntity getById(@PathVariable UUID id) {
    return categoriesService.getById(id);
  }

  @GetMapping("/{id}/products")
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public List<ProductEntity> getProductsForCategory(@PathVariable UUID id) {
    return categoriesService.getProductsForCategory(id);
  }

  @PostMapping("/{id}/products")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void addProductsToCategory(
      @PathVariable UUID id, @RequestBody ProductsIdListDTO productsId) {
    categoriesService.addProductsToCategory(id, productsId);
  }

  @DeleteMapping("/{id}/products/{productId}")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void removeProductFromCategory(@PathVariable UUID id, @PathVariable UUID productId) {
    categoriesService.removeProductFromCategory(id, productId);
  }

  @PostMapping
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void create(@Validated(CreateGroup.class) @RequestBody CategoryEntity category) {
    categoriesService.create(category);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void update(
      @PathVariable UUID id, @Validated(UpdateGroup.class) @RequestBody CategoryEntity category) {
    categoriesService.update(id, category);
  }

  @PostMapping("/{id}/disable")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void disable(@PathVariable UUID id) {
    categoriesService.disable(id);
  }

  @PostMapping("/{id}/enable")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void enable(@PathVariable UUID id) {
    categoriesService.enable(id);
  }
}
