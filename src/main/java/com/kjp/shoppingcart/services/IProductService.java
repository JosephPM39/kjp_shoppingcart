package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
  public Page<ProductEntity> getAll(
      Pageable pageable, SearchProductStrategyEnum strategy, String value);

  public ProductEntity getById(UUID id);

  public void disable(UUID id);

  public void enable(UUID id);

  public void create(ProductEntity product);

  public void update(UUID id, ProductEntity changes);
}
