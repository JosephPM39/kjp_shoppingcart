package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.entities.ProductCategoryEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import com.kjp.shoppingcart.repositories.IProductCategoryRepository;
import com.kjp.shoppingcart.utils.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
  private final ICategoryRepository categoriesRepository;
  private final IProductCategoryRepository productCategoryRepository;

  @Autowired
  public CategoryService(
      ICategoryRepository categoriesRepository,
      IProductCategoryRepository productCategoryRepository) {
    this.categoriesRepository = categoriesRepository;
    this.productCategoryRepository = productCategoryRepository;
  }

  public List<CategoryEntity> getAll() {
    return categoriesRepository.findAll();
  }

  @Override
  public CategoryEntity getById(UUID id) {
    Optional<CategoryEntity> category = categoriesRepository.findById(id);
    if (category.isEmpty()) {
      throw new ResourceNotFoundException(
          "Category with ID: ".concat(id.toString()).concat(" Not Found"));
    }
    return category.get();
  }

  @Override
  public void create(CategoryEntity category) {
    categoriesRepository.save(category);
  }

  @Override
  public void update(UUID id, CategoryEntity category) {
    CategoryEntity oldCategory = this.getById(id);
    CategoryEntity categoryWithChanges =
        ObjectUtils.getInstanceWithNotNullFields(category, oldCategory, CategoryEntity.class);
    categoriesRepository.save(categoryWithChanges);
  }

  @Override
  public void disable(UUID id) {
    CategoryEntity changes = new CategoryEntity();
    changes.setDisabled(true);
    this.update(id, changes);
  }

  @Override
  public void enable(UUID id) {
    CategoryEntity changes = new CategoryEntity();
    changes.setDisabled(false);
    this.update(id, changes);
  }

  @Override
  public void addProductsToCategory(UUID id, ProductsIdListDTO productsId) {
    List<ProductCategoryEntity> productCategoryEntities = new ArrayList<>();
    CategoryEntity category = this.getById(id);

    for (UUID productId : productsId.productsId()) {
      ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();

      if (this.productCategoryRepository.existsByCategoryIdAndProductId(
          category.getId(), productId)) {
        throw new ResourceAlreadyExistsException(
            "The Category with the ID: "
                .concat(id.toString())
                .concat(" already has the product with the ID: ".concat(productId.toString())));
      }

      productCategoryEntity.setProductId(productId);
      productCategoryEntity.setCategoryId(category.getId());

      productCategoryEntities.add(productCategoryEntity);
    }

    this.productCategoryRepository.saveAll(productCategoryEntities);
  }

  @Override
  public void removeProductFromCategory(UUID id, UUID productId) {
    CategoryEntity category = this.getById(id);
    Optional<ProductCategoryEntity> productCategoryEntity =
        this.productCategoryRepository.findFirstByCategoryIdAndProductId(
            category.getId(), productId);
    if (productCategoryEntity.isEmpty()) {
      throw new ResourceNotFoundException(
          "The Category with the ID: "
              .concat(id.toString())
              .concat(" Not has the product with the ID: ".concat(productId.toString())));
    }
  }
}
