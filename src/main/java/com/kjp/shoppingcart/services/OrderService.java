package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.*;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.*;
import com.kjp.shoppingcart.utils.ObjectUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {

  IOrderRepository orderRepository;
  ICartRepository cartRepository;
  IOrderProductRepository orderProductRepository;
  IProductCartRepository productCartRepository;
  IProductRepository productRepository;

  @Autowired
  public OrderService(
      IOrderRepository orderRepository,
      ICartRepository cartRepository,
      IOrderProductRepository orderProductRepository,
      IProductCartRepository productCartRepository,
      IProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.cartRepository = cartRepository;
    this.orderProductRepository = orderProductRepository;
    this.productCartRepository = productCartRepository;
    this.productRepository = productRepository;
  }

  @Override
  public void makeOrderFromUserCart(UUID userId) {
    CartEntity userCart = findUserCart(userId);
    List<ProductCartEntity> productCartEntities =
        productCartRepository.findAllProductsByCartId(userCart.getId());
    if (productCartEntities.isEmpty()) {
      throw new ResourceNotFoundException("The actual user don't has products in the cart");
    }
    List<OrderProductEntity> orderProductEntities = new ArrayList<>();
    OrderEntity order = new OrderEntity();
    order.setStatus(OrderStatusEnum.PENDING);
    order.setTotal(new BigDecimal("0.00"));
    order.setUserId(userId);
    order.setId(UUID.randomUUID());

    for (ProductCartEntity productCartEntity : productCartEntities) {
      OrderProductEntity orderProductEntity = new OrderProductEntity();
      Optional<ProductEntity> productEntity =
          this.productRepository.findById(productCartEntity.getProductId());
      if (productEntity.isEmpty()) {
        throw new ResourceNotFoundException(
            "Product not found with the Id: ".concat(productCartEntity.getProductId().toString()));
      }
      ProductEntity product = productEntity.get();
      orderProductEntity.setOrderId(order.getId());
      orderProductEntity.setProductId(product.getId());
      orderProductEntity.setSoldPrice(product.getPrice());
      orderProductEntity.setQuantity(productCartEntity.getQuantity());
      orderProductEntities.add(orderProductEntity);
      BigDecimal unitPrice = product.getPrice();
      BigDecimal quantity = new BigDecimal(productCartEntity.getQuantity());
      BigDecimal productTotal = unitPrice.multiply(quantity);
      BigDecimal orderTotal = order.getTotal().add(productTotal);
      order.setTotal(orderTotal);
    }

    this.orderRepository.save(order);
    this.orderProductRepository.saveAll(orderProductEntities);
  }

  private CartEntity findUserCart(UUID userId) {
    Optional<CartEntity> userCart = this.cartRepository.findFirstByUserId(userId);
    if (userCart.isEmpty()) {
      throw new ResourceNotFoundException("The actual user don't has a cart");
    }
    return userCart.get();
  }

  private void updateOrder(UUID orderId, OrderEntity changes) {
    Optional<OrderEntity> optionalOrder = this.orderRepository.findById(orderId);
    if (optionalOrder.isEmpty()) {
      throw new ResourceNotFoundException(
          "Order not found with the Id:".concat(orderId.toString()));
    }
    OrderEntity finalOrder =
        ObjectUtils.getInstanceWithNotNullFields(changes, optionalOrder.get(), OrderEntity.class);
    this.orderRepository.save(finalOrder);
  }

  @Override
  public void abortOrder(UUID orderId) {
    OrderEntity changes = new OrderEntity();
    changes.setStatus(OrderStatusEnum.ABORTED);
    updateOrder(orderId, changes);
  }

  @Override
  public void abortOrder(UUID userId, UUID orderId) {
    OrderEntity order = getUserOrder(userId, orderId);
    order.setStatus(OrderStatusEnum.ABORTED);
    this.orderRepository.save(order);
  }

  private OrderEntity getUserOrder(UUID userId, UUID orderId) {
    Optional<OrderEntity> order =
        this.orderRepository.findByUserIdEqualsAndIdEquals(userId, orderId);
    if (order.isPresent()) {
      return order.get();
    }
    throw new ResourceNotFoundException(
        "The actual user don't has a order with the Id: ".concat(orderId.toString()));
  }

  @Override
  public void makeDoneOrder(UUID orderId) {
    OrderEntity changes = new OrderEntity();
    changes.setStatus(OrderStatusEnum.COMPLETED);
    updateOrder(orderId, changes);
  }

  @Override
  public List<OrderEntity> getOrders() {
    return orderRepository.findAll();
  }

  @Override
  public List<OrderEntity> getUserOrders(UUID userId) {
    return orderRepository.findAllByUserIdEquals(userId);
  }

  @Override
  public List<OrderProductEntity> getOrderProducts(UUID orderId) {
    return orderProductRepository.findAllByOrderIdEquals(orderId);
  }

  @Override
  public List<OrderProductEntity> getUserOrderProducts(UUID userId, UUID orderId) {
    OrderEntity userOrder = getUserOrder(userId, orderId);
    return orderProductRepository.findAllByOrderIdEquals(userOrder.getId());
  }
}
