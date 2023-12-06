package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.OrderEntity;
import com.kjp.shoppingcart.entities.OrderStatusEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderEntityFaker {
  public static OrderEntity getFake() {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setStatus(OrderStatusEnum.PENDING);
    orderEntity.setId(UUID.randomUUID());
    orderEntity.setUserId(UUID.randomUUID());
    orderEntity.setTotal(new BigDecimal("100.00"));
    return orderEntity;
  }

  public static List<OrderEntity> getFakes(Integer length) {
    List<OrderEntity> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(getFake());
    }
    return list;
  }
}
