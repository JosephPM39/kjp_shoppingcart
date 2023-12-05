package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.CartEntity;
import com.kjp.shoppingcart.entities.CartStatusEnum;
import java.util.UUID;

public class CartEntityFaker {
  public static CartEntity getFake() {
    CartEntity cart = new CartEntity();
    cart.setId(UUID.randomUUID());
    cart.setUserId(UUID.randomUUID());
    cart.setStatus(CartStatusEnum.PENDING);
    return cart;
  }
}
