package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ProductCategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductCategoryRepository extends IBaseRepository<ProductCategoryEntity, UUID> {
  public Optional<ProductCategoryEntity> findFirstByCategoryIdAndProductId(
      UUID categoryId, UUID productId);

  public List<ProductCategoryEntity> findAllByCategoryIdEquals(UUID categoryId);

  public boolean existsByCategoryIdAndProductId(UUID categoryId, UUID productId);
}
