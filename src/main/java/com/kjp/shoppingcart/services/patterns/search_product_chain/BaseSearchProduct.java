package com.kjp.shoppingcart.services.patterns.search_product_chain;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.BadStrategyConfigException;
import com.kjp.shoppingcart.repositories.IProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Setter
public abstract class BaseSearchProduct implements ISearchProduct {

  private ISearchProduct nextSearch;
  @Getter private IProductRepository productRepository;

  protected BaseSearchProduct(IProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Page<ProductEntity> search(
      String value, Pageable pageable, SearchProductStrategyEnum strategy) {
    if (SearchProductStrategyEnum.NONE != strategy) {
      if (nextSearch == null) {
        throw new BadStrategyConfigException(
            "No chain handler for Search products strategy: ".concat(strategy.name()));
      }
      return nextSearch.search(value, pageable, strategy);
    }
    return productRepository.findAll(pageable);
  }
}
