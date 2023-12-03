package com.kjp.shoppingcart.services;

import java.util.UUID;

public interface IOrderService {
    public void makeOrderFromUserCart(UUID userId);
    public void abortOrder(UUID orderId);
    public void makeDoneOrder(UUID orderId);
    public void getUserOrders(UUID userId);
    public void getUserOrderDetail(UUID orderId);
}
