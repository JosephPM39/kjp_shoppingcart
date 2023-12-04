package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.OrderEntity;
import com.kjp.shoppingcart.entities.OrderProductEntity;
import com.kjp.shoppingcart.services.IOrderService;
import com.kjp.shoppingcart.services.IUserService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final IUserService userService;
  private final IOrderService orderService;

  @Autowired
  public OrderController(IOrderService orderService, IUserService userService) {
    this.orderService = orderService;
    this.userService = userService;
  }

  @PostMapping("/from-shoppingcart")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void makeOrderFromCart() {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.orderService.makeOrderFromUserCart(userId);
  }

  @PostMapping("/user/{orderId}/abort")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void abortUserOrder(@PathVariable("orderId") UUID orderId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.orderService.abortOrder(userId, orderId);
  }

  @PostMapping("/{orderId}/abort")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void abortOrder(@PathVariable("orderId") UUID orderId) {
    this.orderService.abortOrder(orderId);
  }

  @PostMapping("/{orderId}/complete")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public void makeDoneOrder(@PathVariable("orderId") UUID orderId) {
    this.orderService.makeDoneOrder(orderId);
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public List<OrderEntity> getAllUserOrders() {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    return this.orderService.getUserOrders(userId);
  }

  @GetMapping("/")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public List<OrderEntity> getAllOrders() {
    return this.orderService.getOrders();
  }

  @GetMapping("/{orderId}/products")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
  public List<OrderProductEntity> getOrderProducts(@PathVariable("orderId") UUID orderId) {
    return this.orderService.getOrderProducts(orderId);
  }

  @GetMapping("/user/{orderId}/products")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public List<OrderProductEntity> getUserOrderProducts(@PathVariable("orderId") UUID orderId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    return this.orderService.getUserOrderProducts(userId, orderId);
  }
}
