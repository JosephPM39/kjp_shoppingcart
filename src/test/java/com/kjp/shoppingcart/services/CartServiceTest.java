package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kjp.shoppingcart.dto.ProductCartDTO;
import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CartEntity;
import com.kjp.shoppingcart.entities.ProductCartEntity;
import com.kjp.shoppingcart.exceptions.BadProductCartQuantityException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.fakers.CartEntityFaker;
import com.kjp.shoppingcart.fakers.ProductCartEntityFaker;
import com.kjp.shoppingcart.fakers.ProductsIdListDTOFaker;
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
class CartServiceTest {

  @Autowired ICartService cartService;

  @MockBean IProductRepository productRepository;
  @MockBean ICartRepository cartRepository;
  @MockBean IOrderRepository orderRepository;
  @MockBean IOrderProductRepository orderProductRepository;
  @MockBean IProductCartRepository productCartRepository;

  @Test
  void addProductsToUserCart() {
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.empty());

    ProductsIdListDTO productsIdListDTO = ProductsIdListDTOFaker.getFake(10, true);
    assertEquals(10, productsIdListDTO.productsId().length);

    when(productRepository.existsById(any(UUID.class))).thenReturn(true);

    Optional<ProductCartEntity> productCart = Optional.of(new ProductCartEntity());
    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(productCart);

    cartService.addProductsToUserCart(UUID.randomUUID(), productsIdListDTO);

    verify(cartRepository).findFirstByUserId(any(UUID.class));
    verify(productRepository, times(9)).existsById(any(UUID.class));
    verify(productCartRepository, times(9))
        .findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void addProductsToUserCartPreExists() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    ProductsIdListDTO productsIdListDTO = ProductsIdListDTOFaker.getFake(10, true);
    assertEquals(10, productsIdListDTO.productsId().length);

    when(productRepository.existsById(any(UUID.class))).thenReturn(true);

    Optional<ProductCartEntity> productCart = Optional.of(new ProductCartEntity());
    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(productCart);

    cartService.addProductsToUserCart(cartEntity.getUserId(), productsIdListDTO);

    verify(cartRepository).findFirstByUserId(any(UUID.class));
    verify(productRepository, times(9)).existsById(any(UUID.class));
    verify(productCartRepository, times(9))
        .findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeAllOfProductFromCart() {
    Optional<ProductCartEntity> productCart = Optional.of(new ProductCartEntity());
    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(productCart);

    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    cartService.removeAllOfProductFromCart(cartEntity.getId(), UUID.randomUUID());
    verify(productCartRepository).findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void getAllCartProducts() {
    when(productCartRepository.findAllProductsByCartId(any(UUID.class)))
        .thenReturn(new ArrayList<>());

    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    List<ProductCartDTO> productCartEntities =
        cartService.getAllCartProducts(cartEntity.getUserId());

    assertNotNull(productCartEntities);

    verify(productCartRepository).findAllProductsByCartId(any(UUID.class));
    verify(cartRepository).findFirstByUserId(any(UUID.class));
  }

  @Test
  void removeProductFromCart() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    ProductCartEntity productCartEntity = ProductCartEntityFaker.getFake();
    productCartEntity.setCartId(cartEntity.getId());
    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(productCartEntity));

    cartService.removeProductFromCart(cartEntity.getUserId(), productCartEntity.getProductId(), 1);

    verify(cartRepository).findFirstByUserId(any(UUID.class));
    verify(productCartRepository).findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductFromCartWithNotExistingInCart() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      cartService.removeProductFromCart(cartEntity.getUserId(), UUID.randomUUID(), 1);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(cartRepository).findFirstByUserId(any(UUID.class));
    verify(productCartRepository).findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductFromCartWithEqualQuantity() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    ProductCartEntity productCartEntity = ProductCartEntityFaker.getFake();
    productCartEntity.setCartId(cartEntity.getId());
    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(productCartEntity));

    cartService.removeProductFromCart(
        cartEntity.getUserId(), productCartEntity.getProductId(), productCartEntity.getQuantity());

    verify(cartRepository).findFirstByUserId(any(UUID.class));
    verify(productCartRepository, times(2))
        .findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductFromCartWithMajorQuantity() {
    CartEntity cartEntity = CartEntityFaker.getFake();
    when(cartRepository.findFirstByUserId(any(UUID.class))).thenReturn(Optional.of(cartEntity));

    ProductCartEntity productCartEntity = ProductCartEntityFaker.getFake();
    when(productCartRepository.findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(productCartEntity));

    try {
      cartService.removeProductFromCart(
          cartEntity.getUserId(),
          productCartEntity.getProductId(),
          productCartEntity.getQuantity() + 1);
    } catch (Exception e) {
      assertInstanceOf(BadProductCartQuantityException.class, e);
    }

    verify(cartRepository).findFirstByUserId(any(UUID.class));
    verify(productCartRepository).findFirstByCartIdAndProductId(any(UUID.class), any(UUID.class));
  }
}
