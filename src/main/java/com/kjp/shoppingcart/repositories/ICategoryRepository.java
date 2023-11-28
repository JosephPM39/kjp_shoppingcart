package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, UUID> {

}
