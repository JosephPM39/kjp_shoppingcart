package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    ICartService cartService;

    @Test
    void addProductsToUserCart() {

    }

    @Test
    void removeAllOfProductFromCart() {

    }

    @Test
    void getAllCartProducts() {
    }

    @Test
    void removeProductFromCart() {
    }
}