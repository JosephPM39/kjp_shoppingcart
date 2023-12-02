package com.kjp.shoppingcart.services.patterns.search_product_chain;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.repositories.IProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SearchProductByNameContainsHandler extends BaseSearchProduct {

    protected SearchProductByNameContainsHandler(IProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    public Page<ProductEntity> search(String value, Pageable pageable, SearchProductStrategyEnum strategy) {
        if (SearchProductStrategyEnum.BY_NAME_CONTAINS != strategy) {
            return super.search(value, pageable, strategy);
        }
        return super.getProductRepository().findByNameContainsIgnoreCase(value, pageable);
    }
}
