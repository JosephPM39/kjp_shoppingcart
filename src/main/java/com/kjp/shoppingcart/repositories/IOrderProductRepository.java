package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.OrderProductEntity;

import java.util.List;
import java.util.UUID;

public interface IOrderProductRepository extends IBaseRepository<OrderProductEntity, UUID> {
    public List<OrderProductEntity> findAllByOrderIdEquals(UUID orderId);
}
