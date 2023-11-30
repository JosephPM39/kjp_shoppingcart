package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    public List<ProductEntity> getAll() {
        Specification<Object> test;
        return List.of();
    }
}
