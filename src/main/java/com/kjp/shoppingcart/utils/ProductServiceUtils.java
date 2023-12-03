package com.kjp.shoppingcart.utils;

import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.IProductRepository;

import java.util.List;
import java.util.UUID;

public class ProductServiceUtils {
    public static void throwIfProductNotFound(UUID productId, IProductRepository productRepository) {
        boolean isProductExists = productRepository.existsById(productId);
        if (isProductExists) {
            return;
        }
        throw new ResourceNotFoundException("The product with the Id:".concat(productId.toString()).concat(" not found"));
    }

    public static void throwIfSomeProductNotFound(List<UUID> productIdList, IProductRepository productRepository) {
        for(UUID productId :productIdList) {
            throwIfProductNotFound(productId, productRepository);
        }
    }
}
