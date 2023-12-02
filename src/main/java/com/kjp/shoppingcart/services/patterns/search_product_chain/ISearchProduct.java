package com.kjp.shoppingcart.services.patterns.search_product_chain;

import com.kjp.shoppingcart.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISearchProduct {
    public Page<ProductEntity> search(String value, Pageable pageable, SearchProductStrategyEnum strategy);
    public void setNextSearch(ISearchProduct search);
}
