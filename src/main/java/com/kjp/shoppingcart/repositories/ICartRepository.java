package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.CartEntity;
import java.util.Optional;
import java.util.UUID;

public interface ICartRepository extends IBaseRepository<CartEntity, UUID> {
  public Optional<CartEntity> findFirstByUserId(UUID userId);
}
