package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CategoryEntity;

import java.util.UUID;

public interface ICategoryService {

    public CategoryEntity getById(UUID id);

    public void create(CategoryEntity category);

    public void update(UUID id, CategoryEntity category);

    public void disable(UUID id);

    public void enable(UUID id);

    public void addProductsToCategory(UUID id, ProductsIdListDTO productsId);

    public void removeProductFromCategory(UUID id, UUID productId);

}
