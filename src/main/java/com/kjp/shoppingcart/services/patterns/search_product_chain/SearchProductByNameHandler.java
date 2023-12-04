package com.kjp.shoppingcart.services.patterns.search_product_chain;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.repositories.IProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SearchProductByNameHandler extends BaseSearchProduct {

  protected SearchProductByNameHandler(IProductRepository productRepository) {
    super(productRepository);
  }

  @Override
  public Page<ProductEntity> search(
      String value, Pageable pageable, SearchProductStrategyEnum strategy) {
    if (SearchProductStrategyEnum.BY_NAME != strategy) {
      return super.search(value, pageable, strategy);
    }
    return super.getProductRepository().findByName(value, pageable);
  }
}
