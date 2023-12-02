package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.PaginateProductDTO;
import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.repositories.IProductRepository;
import com.kjp.shoppingcart.services.patterns.product_search_strategy.OrderEnum;
import com.kjp.shoppingcart.services.patterns.product_search_strategy.Pagination;
import com.kjp.shoppingcart.services.patterns.product_search_strategy.SearchProductByName;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductChain;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final IProductRepository productRepository;
    private final SearchProductChain searchProductChain;

    @Autowired
    public ProductService(IProductRepository productRepository) {
       this.productRepository = productRepository;
       this.searchProductChain = new SearchProductChain(this.productRepository);
    }

    public Page<ProductEntity> getAll(Pageable pageable, SearchProductStrategyEnum strategy, String value) {
        return this.searchProductChain.search(value, pageable, strategy);
    }

    public Page<ProductEntity> getByName(String name, Pageable pageable) {
        return productRepository.findByName(name, pageable);
    }

    public Page<ProductEntity> getByCategoryName(String name, Pageable pageable) {
        return productRepository.findByCategoryName(name, pageable);
    }

    public ProductEntity getById(UUID id) {
        return productRepository.findById(id).get();
    }

    public void voteProduct() {
    }

    public void disable() {
    }

    public void remove() {
    }

    public void update() {
    }


}
