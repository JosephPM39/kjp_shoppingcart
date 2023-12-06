package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kjp.shoppingcart.dto.ProductsIdListDTO;
import com.kjp.shoppingcart.entities.CategoryEntity;
import com.kjp.shoppingcart.entities.ProductCategoryEntity;
import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.fakers.CategoryEntityFaker;
import com.kjp.shoppingcart.fakers.ProductCategoryEntityFaker;
import com.kjp.shoppingcart.fakers.ProductEntityFaker;
import com.kjp.shoppingcart.fakers.ProductsIdListDTOFaker;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import com.kjp.shoppingcart.repositories.IProductCategoryRepository;
import com.kjp.shoppingcart.repositories.IProductRepository;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CategoryServiceTest {

  @Autowired ICategoryService categoryService;
  @MockBean private ICategoryRepository categoriesRepositoryMock;
  @MockBean private IProductCategoryRepository productCategoryRepositoryMock;
  @MockBean private IProductRepository productRepositoryMock;

  @Test
  void getAll() {
    when(categoriesRepositoryMock.findAll()).thenReturn(CategoryEntityFaker.getFakes(10));
    List<CategoryEntity> categoryEntities = categoryService.getAll();
    assertEquals(10, categoryEntities.size());
    verify(categoriesRepositoryMock).findAll();
  }

  @Test
  void getById() {
    CategoryEntity categoryMock = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(categoryMock));

    CategoryEntity category = categoryService.getById(categoryMock.getId());
    assertNotNull(category);
    assertEquals(categoryMock.getId(), category.getId());

    verify(categoriesRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void getByIdNotFound() {
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

    try {
      categoryService.getById(UUID.randomUUID());
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(categoriesRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void create() {
    categoryService.create(CategoryEntityFaker.getFake());
    verify(categoriesRepositoryMock).save(any(CategoryEntity.class));
  }

  @Test
  void update() {
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));
    categoryService.update(category.getId(), category);
    verify(categoriesRepositoryMock).save(any(CategoryEntity.class));
    verify(categoriesRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void disable() {
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));
    categoryService.disable(category.getId());
    verify(categoriesRepositoryMock).save(any(CategoryEntity.class));
    verify(categoriesRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void enable() {
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));
    categoryService.enable(category.getId());
    verify(categoriesRepositoryMock).save(any(CategoryEntity.class));
    verify(categoriesRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void addProductsToCategory() {
    ProductsIdListDTO productsIdListDTO = ProductsIdListDTOFaker.getFake(10, false);
    when(productCategoryRepositoryMock.existsByCategoryIdAndProductId(
            any(UUID.class), any(UUID.class)))
        .thenReturn(false);
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));

    categoryService.addProductsToCategory(category.getId(), productsIdListDTO);

    verify(categoriesRepositoryMock).findById(any(UUID.class));
    verify(productCategoryRepositoryMock, times(10))
        .existsByCategoryIdAndProductId(any(UUID.class), any(UUID.class));
    verify(productCategoryRepositoryMock).saveAll(any());
  }

  @Test
  void addProductsToCategoryThrowAlreadyExistProductCategory() {
    ProductsIdListDTO productsIdListDTO = ProductsIdListDTOFaker.getFake(10, true);
    when(productCategoryRepositoryMock.existsByCategoryIdAndProductId(
            any(UUID.class), any(UUID.class)))
        .thenReturn(true);
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));

    try {
      categoryService.addProductsToCategory(category.getId(), productsIdListDTO);
    } catch (Exception e) {
      assertInstanceOf(ResourceAlreadyExistsException.class, e);
    }

    verify(categoriesRepositoryMock).findById(any(UUID.class));
    verify(productCategoryRepositoryMock)
        .existsByCategoryIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductFromCategory() {
    ProductCategoryEntity productCategoryEntity = ProductCategoryEntityFaker.getFake();
    when(productCategoryRepositoryMock.findFirstByCategoryIdAndProductId(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(productCategoryEntity));
    CategoryEntity category = CategoryEntityFaker.getFake();
    category.setId(productCategoryEntity.getCategoryId());
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));

    categoryService.removeProductFromCategory(
        category.getId(), productCategoryEntity.getProductId());

    verify(productCategoryRepositoryMock)
        .findFirstByCategoryIdAndProductId(any(UUID.class), any(UUID.class));
    verify(productCategoryRepositoryMock).deleteById(any(UUID.class));
    verify(categoriesRepositoryMock).findById(any(UUID.class));
  }

  @Test
  void removeProductFromCategoryWithNotProduct() {
    when(productCategoryRepositoryMock.findFirstByCategoryIdAndProductId(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));
    try {
      categoryService.removeProductFromCategory(category.getId(), UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }
    verify(categoriesRepositoryMock).findById(any(UUID.class));
    verify(productCategoryRepositoryMock)
        .findFirstByCategoryIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void getProductsFromCategory() {
    List<ProductCategoryEntity> productCategoryEntities = ProductCategoryEntityFaker.getFakes(10);
    when(productCategoryRepositoryMock.findAllByCategoryIdEquals(any(UUID.class)))
        .thenReturn(productCategoryEntities);
    ProductEntity product = ProductEntityFaker.getFake();
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(product));

    List<ProductEntity> products = categoryService.getProductsForCategory(UUID.randomUUID());

    assertEquals(productCategoryEntities.size(), products.size());

    verify(productCategoryRepositoryMock).findAllByCategoryIdEquals(any(UUID.class));
    verify(productRepositoryMock, times(10)).findById(any(UUID.class));
  }

  @Test
  void getProductsFromCategoryInternalError() {
    List<ProductCategoryEntity> productCategoryEntities = ProductCategoryEntityFaker.getFakes(10);
    when(productCategoryRepositoryMock.findAllByCategoryIdEquals(any(UUID.class)))
        .thenReturn(productCategoryEntities);
    when(productRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());
    CategoryEntity category = CategoryEntityFaker.getFake();
    when(categoriesRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.of(category));

    try {
      List<ProductEntity> products = categoryService.getProductsForCategory(category.getId());
    } catch (Exception e) {
      assertInstanceOf(InternalServerErrorException.class, e);
    }

    verify(productCategoryRepositoryMock).findAllByCategoryIdEquals(any(UUID.class));
    verify(productRepositoryMock).findById(any(UUID.class));
  }
}
