package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kjp.shoppingcart.entities.*;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.fakers.*;
import com.kjp.shoppingcart.repositories.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OrderServiceTest {

  @MockBean private IOrderRepository orderRepositoryMock;
  @MockBean private ICartRepository cartRepositoryMock;
  @MockBean private IOrderProductRepository orderProductRepositoryMock;
  @MockBean private IProductCartRepository productCartRepositoryMock;
  @MockBean private IProductRepository productRepositoryMock;

  @Autowired private IOrderService orderService;

  @Test
  void makeOrderFromUserCart() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepositoryMock.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));
    List<ProductCartEntity> productCartEntities =
        ProductCartEntityFaker.getFakes(cartEntity.getId(), 10);
    when(productCartRepositoryMock.findAllProductsByCartId(any(UUID.class)))
        .thenReturn(productCartEntities);
    ProductEntity product = ProductEntityFaker.getFake();
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product));

    orderService.makeOrderFromUserCart(UUID.randomUUID());

    verify(orderProductRepositoryMock).saveAll(any());
    verify(orderRepositoryMock).save(any(OrderEntity.class));
    verify(cartRepositoryMock).findFirstByUserId(any(UUID.class));
    verify(productCartRepositoryMock).findAllProductsByCartId(any(UUID.class));
    verify(productRepositoryMock, times(10)).findById(any(UUID.class));
  }

  @Test
  void makeOrderFromUserCartNotFound() {
    when(cartRepositoryMock.findFirstByUserId(any(UUID.class))).thenReturn(Optional.empty());

    try {
      orderService.makeOrderFromUserCart(UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(cartRepositoryMock).findFirstByUserId(any(UUID.class));
  }

  @Test
  void makeOrderFromUserCartProductListNotFound() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepositoryMock.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));
    when(productCartRepositoryMock.findAllProductsByCartId(any(UUID.class)))
        .thenReturn(new ArrayList<>());

    try {
      orderService.makeOrderFromUserCart(UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(cartRepositoryMock).findFirstByUserId(any(UUID.class));
    verify(productCartRepositoryMock).findAllProductsByCartId(any(UUID.class));
  }

  @Test
  void makeOrderFromUserCartProductNotFound() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepositoryMock.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));
    List<ProductCartEntity> productCartEntities =
        ProductCartEntityFaker.getFakes(cartEntity.getId(), 10);
    when(productCartRepositoryMock.findAllProductsByCartId(any(UUID.class)))
        .thenReturn(productCartEntities);
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

    try {
      orderService.makeOrderFromUserCart(UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(cartRepositoryMock).findFirstByUserId(any(UUID.class));
    verify(productCartRepositoryMock).findAllProductsByCartId(any(UUID.class));
    verify(productRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void abortOrder() {
    OrderEntity orderEntity = OrderEntityFaker.getFake();
    when(orderRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

    orderService.abortOrder(orderEntity.getId());

    verify(orderRepositoryMock).findById(any(UUID.class));
    verify(orderRepositoryMock).save(any(OrderEntity.class));
  }

  @Test
  void abortOrderNotFound() {
    when(orderRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

    try {
      orderService.abortOrder(UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(orderRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void AbortOrderAdmin() {
    OrderEntity orderEntity = OrderEntityFaker.getFake();
    when(orderRepositoryMock.findByUserIdEqualsAndIdEquals(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(orderEntity));

    orderService.abortOrder(orderEntity.getUserId(), orderEntity.getId());

    verify(orderRepositoryMock).findByUserIdEqualsAndIdEquals(any(UUID.class), any(UUID.class));
    verify(orderRepositoryMock).save(any(OrderEntity.class));
  }

  @Test
  void AbortOrderAdminNotFound() {
    when(orderRepositoryMock.findByUserIdEqualsAndIdEquals(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      orderService.abortOrder(UUID.randomUUID(), UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(orderRepositoryMock).findByUserIdEqualsAndIdEquals(any(UUID.class), any(UUID.class));
  }

  @Test
  void makeDoneOrder() {
    OrderEntity orderEntity = OrderEntityFaker.getFake();
    when(orderRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

    orderService.makeDoneOrder(orderEntity.getId());

    verify(orderRepositoryMock).findById(any(UUID.class));
    verify(orderRepositoryMock).save(any(OrderEntity.class));
  }

  @Test
  void getOrders() {
    List<OrderEntity> list = OrderEntityFaker.getFakes(10);
    when(orderRepositoryMock.findAll()).thenReturn(list);

    List<OrderEntity> orderEntities = orderService.getOrders();
    assertNotNull(orderEntities);
    assertEquals(list.size(), orderEntities.size());

    verify(orderRepositoryMock).findAll();
  }

  @Test
  void getUserOrders() {
    List<OrderEntity> list = OrderEntityFaker.getFakes(10);
    when(orderRepositoryMock.findAllByUserIdEquals(any(UUID.class))).thenReturn(list);

    List<OrderEntity> orderEntities = orderService.getUserOrders(UUID.randomUUID());
    assertNotNull(orderEntities);
    assertEquals(list.size(), orderEntities.size());

    verify(orderRepositoryMock).findAllByUserIdEquals(any(UUID.class));
  }

  @Test
  void getOrderProducts() {
    List<OrderProductEntity> list = OrderProductEntityFaker.getFakes(10);
    when(orderProductRepositoryMock.findAllByOrderIdEquals(any(UUID.class))).thenReturn(list);

    List<OrderProductEntity> orderProductEntities =
        orderService.getOrderProducts(UUID.randomUUID());

    assertNotNull(orderProductEntities);
    assertEquals(list.size(), orderProductEntities.size());

    verify(orderProductRepositoryMock).findAllByOrderIdEquals(any(UUID.class));
  }

  @Test
  void getUserOrderProducts() {
    OrderEntity orderEntity = OrderEntityFaker.getFake();
    when(orderRepositoryMock.findByUserIdEqualsAndIdEquals(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(orderEntity));
    List<OrderProductEntity> list = OrderProductEntityFaker.getFakes(10);
    when(orderProductRepositoryMock.findAllByOrderIdEquals(any(UUID.class))).thenReturn(list);

    List<OrderProductEntity> orderProductEntities =
        orderService.getUserOrderProducts(orderEntity.getUserId(), orderEntity.getId());

    assertNotNull(orderProductEntities);
    assertEquals(list.size(), orderProductEntities.size());

    verify(orderProductRepositoryMock).findAllByOrderIdEquals(any(UUID.class));
    verify(orderRepositoryMock).findByUserIdEqualsAndIdEquals(any(UUID.class), any(UUID.class));
  }
}
