package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.OrderEntity;
import com.kjp.shoppingcart.entities.OrderProductEntity;

import java.util.List;
import java.util.UUID;

public interface IOrderRepository extends IBaseRepository<OrderEntity, UUID> {
    public List<OrderEntity> findAllByUserIdEquals(UUID orderId);
}
