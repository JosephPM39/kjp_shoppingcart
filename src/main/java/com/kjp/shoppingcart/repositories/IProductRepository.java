package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<ProductEntity, UUID> {
}
