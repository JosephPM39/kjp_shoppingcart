package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
