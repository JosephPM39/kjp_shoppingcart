package com.kjp.shoppingcart.services.patterns.search_product_chain;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.repositories.IProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SearchProductChain {
  private IProductRepository productRepository;

  public SearchProductChain(IProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<ProductEntity> search(
      String value, Pageable pageable, SearchProductStrategyEnum strategy) {
    ISearchProduct searchByName = new SearchProductByNameHandler(this.productRepository);
    ISearchProduct searchByKeyword = new SearchProductByKeyWordHandler(this.productRepository);
    ISearchProduct searchByCategoryName =
        new SearchProductByCategoryNameHandler(this.productRepository);
    ISearchProduct searchByNameContains =
        new SearchProductByNameContainsHandler(this.productRepository);

    searchByKeyword.setNextSearch(searchByCategoryName);
    searchByNameContains.setNextSearch(searchByKeyword);
    searchByName.setNextSearch(searchByNameContains);

    return searchByName.search(value, pageable, strategy);
  }
}
