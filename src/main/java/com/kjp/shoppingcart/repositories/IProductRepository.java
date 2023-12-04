package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ProductEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IProductRepository extends IBaseRepository<ProductEntity, UUID> {

  public Page<ProductEntity> findByName(String name, Pageable pageable);

  public Page<ProductEntity> findByNameContainsIgnoreCase(String name, Pageable pageable);

  @Query("SELECT p FROM products p JOIN p.categories c WHERE c.name = :categoryName")
  public Page<ProductEntity> findByCategoryName(
      @Param("categoryName") String categoryName, Pageable pageable);

  public Page<ProductEntity> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(
      String name, String description, Pageable pageable);
}
