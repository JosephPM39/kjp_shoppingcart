package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.fakers.ProductEntityFaker;
import com.kjp.shoppingcart.repositories.IProductRepository;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class ProductServiceTest {

  @MockBean private IProductRepository productRepositoryMock;

  @Autowired private IProductService productService;

  @Test
  void getAll() {
    Page<ProductEntity> products =
        productService.getAll(Pageable.ofSize(10), SearchProductStrategyEnum.NONE, null);

    assertNull(products);

    verify(productRepositoryMock).findAll(any(Pageable.class));
  }

  @Test
  void getById() {
    ProductEntity product = ProductEntityFaker.getFake();
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product));

    ProductEntity productEntity = productService.getById(product.getId());

    assertNotNull(productEntity);
    assertEquals(product.getId(), productEntity.getId());

    verify(productRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void getByIdNotFound() {
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

    try {
      ProductEntity productEntity = productService.getById(UUID.randomUUID());
      assertNull(productEntity);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(productRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void disable() {
    ProductEntity product = ProductEntityFaker.getFake();
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product));

    productService.disable(product.getId());

    verify(productRepositoryMock).findById(any(UUID.class));
    verify(productRepositoryMock).save(any(ProductEntity.class));
  }

  @Test
  void enable() {
    ProductEntity product = ProductEntityFaker.getFake();
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product));

    productService.enable(product.getId());

    verify(productRepositoryMock).findById(any(UUID.class));
    verify(productRepositoryMock).save(any(ProductEntity.class));
  }

  @Test
  void create() {
    ProductEntity product = ProductEntityFaker.getFake();

    productService.create(product);

    verify(productRepositoryMock).save(any(ProductEntity.class));
  }

  @Test
  void update() {
    ProductEntity product = ProductEntityFaker.getFake();
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product));
    ProductEntity changes = ProductEntityFaker.getFake();
    changes.setId(product.getId());

    productService.update(changes.getId(), changes);

    verify(productRepositoryMock).findById(any(UUID.class));
    verify(productRepositoryMock).save(any(ProductEntity.class));
  }
}
