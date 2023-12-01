package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IProductRepository extends IBaseRepository<ProductEntity, UUID> {
    public Page<ProductEntity> findByName(String name, Pageable pageable);
    @Query("SELECT p FROM products p JOIN p.categories c WHERE c.name = :categoryName")
    public Page<ProductEntity> findByCategoryName(String categoryName, Pageable pageable);

    public Page<ProductEntity> findByNameOrDescriptionContains(String name, String description, Pageable pageable);
}
