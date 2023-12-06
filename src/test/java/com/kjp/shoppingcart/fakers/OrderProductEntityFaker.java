package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.OrderProductEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderProductEntityFaker {
  public static OrderProductEntity getFake() {
    OrderProductEntity orderProduct = new OrderProductEntity();
    orderProduct.setProductId(UUID.randomUUID());
    orderProduct.setOrderId(UUID.randomUUID());
    orderProduct.setQuantity(5);
    orderProduct.setSoldPrice(new BigDecimal("100.00"));
    return orderProduct;
  }

  public static List<OrderProductEntity> getFakes(Integer length) {
    List<OrderProductEntity> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(getFake());
    }
    return list;
  }
}
