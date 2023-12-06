package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.ProductCartEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductCartEntityFaker {
  public static ProductCartEntity getFake() {
    ProductCartEntity productCartEntity = new ProductCartEntity();
    productCartEntity.setProductId(UUID.randomUUID());
    productCartEntity.setCartId(UUID.randomUUID());
    productCartEntity.setQuantity(5);
    return productCartEntity;
  }

  public static ProductCartEntity getFake(UUID cartId) {
    ProductCartEntity productCartEntity = getFake();
    productCartEntity.setCartId(cartId);
    return productCartEntity;
  }

  public static List<ProductCartEntity> getFakes(UUID cartId, Integer length) {
    List<ProductCartEntity> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(getFake(cartId));
    }
    return list;
  }
}
