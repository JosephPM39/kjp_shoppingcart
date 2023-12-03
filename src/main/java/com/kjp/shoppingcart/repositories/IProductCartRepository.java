package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ProductCartEntity;
import com.kjp.shoppingcart.entities.ProductEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductCartRepository extends IBaseRepository<ProductCartEntity, UUID> {
    public Optional<ProductCartEntity> findFirstByCartIdAndProductId(UUID cartId, UUID productId);

    public boolean existsByCartIdAndProductId(UUID cartId, UUID productId);

    public void deleteByCartIdAndProductId(UUID cartId, UUID productId);

    @Query("SELECT pc FROM products_carts pc JOIN pc.product WHERE pc.cartId = :cardId ")
    public List<ProductCartEntity> findAllProductsByCartId(UUID cartId);

}
