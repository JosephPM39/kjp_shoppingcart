package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IBrandRepository extends JpaRepository<BrandEntity, UUID> {
}
