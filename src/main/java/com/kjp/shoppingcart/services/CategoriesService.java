package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.Categories;
import com.kjp.shoppingcart.repositories.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private final ICategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(ICategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Categories> getAll() {
        return categoriesRepository.findAll();
    }
}
