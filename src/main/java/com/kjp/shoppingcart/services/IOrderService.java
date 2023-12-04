package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.OrderEntity;
import com.kjp.shoppingcart.entities.OrderProductEntity;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
  public void makeOrderFromUserCart(UUID userId);

  public void abortOrder(UUID orderId);

  public void abortOrder(UUID userId, UUID orderId);

  public void makeDoneOrder(UUID orderId);

  public List<OrderEntity> getOrders();

  public List<OrderEntity> getUserOrders(UUID userId);

  public List<OrderProductEntity> getOrderProducts(UUID orderId);

  public List<OrderProductEntity> getUserOrderProducts(UUID userId, UUID orderId);
}
