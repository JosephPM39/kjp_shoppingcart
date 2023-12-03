package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.ProductCartDTO;
import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.services.ICartService;
import com.kjp.shoppingcart.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class CartController {

    ICartService cartService;
    IUserService userService;

    @Autowired
    public CartController(ICartService cartService, IUserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/products")
    public void addProductsToCart(@RequestBody ProductsIdListDTO productsIdListDTO) {
        UUID userId = userService.getAuthenticatedLocalUserId();
        this.cartService.addProductsToUserCart(userId, productsIdListDTO);
    }

    @DeleteMapping("/products/{productId}/{quantity}")
    public void removeProductFromCart(
        @PathVariable("productId") UUID productId,
        @PathVariable("quantity") Integer quantity
    ) {
       UUID userId = userService.getAuthenticatedLocalUserId();
       this.cartService.removeProductFromCart(userId, productId, quantity);
    }

    @DeleteMapping("/products/{productId}/all")
    public void removeAllOfProductFromCart(@PathVariable("productId") UUID productId) {
        UUID userId = userService.getAuthenticatedLocalUserId();
        this.cartService.removeAllOfProductFromCart(userId, productId);
    }

    @GetMapping("/products")
    public List<ProductCartDTO> getAllCartProducts() {
        UUID userId = userService.getAuthenticatedLocalUserId();
        return this.cartService.getAllCartProducts(userId);
    }

}
