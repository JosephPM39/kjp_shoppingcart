package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderProductRepository extends JpaRepository<OrderProductEntity, UUID> {
}
