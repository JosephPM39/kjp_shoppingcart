package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.ProductCategoryEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductCategoryEntityFaker {
  public static ProductCategoryEntity getFake() {
    ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
    productCategoryEntity.setProductId(UUID.randomUUID());
    productCategoryEntity.setCategoryId(UUID.randomUUID());
    productCategoryEntity.setId(UUID.randomUUID());
    return productCategoryEntity;
  }

  public static List<ProductCategoryEntity> getFakes(Integer length) {
    List<ProductCategoryEntity> productCategoryEntities = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      productCategoryEntities.add(getFake());
    }
    return productCategoryEntities;
  }
}
