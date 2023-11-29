package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private final ICategoryRepository categoriesRepository;

    @Autowired
    public CategoriesService(ICategoryRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<CategoryEntity> getAll() {
        return categoriesRepository.findAll();
    }

    
}
