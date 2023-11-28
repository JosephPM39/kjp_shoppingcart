package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICartRepository extends JpaRepository<CartEntity, UUID> {
}
