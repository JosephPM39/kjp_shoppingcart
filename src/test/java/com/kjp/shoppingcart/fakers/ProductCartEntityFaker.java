package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.ProductCartEntity;
import java.util.UUID;

public class ProductCartEntityFaker {
  public static ProductCartEntity getFake() {
    ProductCartEntity productCartEntity = new ProductCartEntity();
    productCartEntity.setProductId(UUID.randomUUID());
    productCartEntity.setCartId(UUID.randomUUID());
    productCartEntity.setQuantity(5);
    return productCartEntity;
  }
}
