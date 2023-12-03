package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.entities.OrderEntity;
import com.kjp.shoppingcart.entities.OrderProductEntity;
import com.kjp.shoppingcart.services.ICartService;
import com.kjp.shoppingcart.services.IOrderService;
import com.kjp.shoppingcart.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    IUserService userService;
    IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService, IUserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/from-shoppingcart")
    public void makeOrderFromCart() {
        UUID userId = this.userService.getAuthenticatedLocalUserId();
        this.orderService.makeOrderFromUserCart(userId);
    }

    @PostMapping("/user/{orderId}/abort")
    public void abortUserOrder(@PathVariable("orderId") UUID orderId) {
        UUID userId = this.userService.getAuthenticatedLocalUserId();
        this.orderService.abortOrder(userId, orderId);
    }


    @PostMapping("/{orderId}/abort")
    public void abortOrder(@PathVariable("orderId") UUID orderId) {
        this.orderService.abortOrder(orderId);
    }

    @PostMapping("/{orderId}/complete")
    public void makeDoneOrder(@PathVariable("orderId") UUID orderId) {
        this.orderService.makeDoneOrder(orderId);
    }

    @GetMapping("/user")
    public List<OrderEntity> getAllUserOrders() {
        UUID userId = this.userService.getAuthenticatedLocalUserId();
        return this.orderService.getUserOrders(userId);
    }

    @GetMapping("/")
    public List<OrderEntity> getAllOrders() {
        return this.orderService.getOrders();
    }

    @GetMapping("/{orderId}/products")
    public List<OrderProductEntity> getOrderProducts(@PathVariable("orderId") UUID orderId) {
        return this.orderService.getOrderProducts(orderId);
    }

    @GetMapping("/user/{orderId}/products")
    public List<OrderProductEntity> getUserOrderProducts(@PathVariable("orderId") UUID orderId) {
        UUID userId = this.userService.getAuthenticatedLocalUserId();
        return this.orderService.getUserOrderProducts(userId, orderId);
    }

}
