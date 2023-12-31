package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.OrderEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository extends IBaseRepository<OrderEntity, UUID> {
  public List<OrderEntity> findAllByUserIdEquals(UUID userId);

  public Optional<OrderEntity> findByUserIdEqualsAndIdEquals(UUID userId, UUID orderId);
}
