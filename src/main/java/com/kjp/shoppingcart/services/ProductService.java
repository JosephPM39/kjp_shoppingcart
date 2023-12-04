package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.entities.ProductEntity;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.repositories.IProductRepository;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductChain;
import com.kjp.shoppingcart.services.patterns.search_product_chain.SearchProductStrategyEnum;
import com.kjp.shoppingcart.utils.ObjectUtils;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {

  private final IProductRepository productRepository;
  private final SearchProductChain searchProductChain;

  @Autowired
  public ProductService(IProductRepository productRepository) {
    this.productRepository = productRepository;
    this.searchProductChain = new SearchProductChain(this.productRepository);
  }

  @Override
  public Page<ProductEntity> getAll(
      Pageable pageable, SearchProductStrategyEnum strategy, String value) {
    return this.searchProductChain.search(value, pageable, strategy);
  }

  @Override
  public ProductEntity getById(UUID id) {
    Optional<ProductEntity> product = productRepository.findById(id);
    if (product.isEmpty()) {
      throw new ResourceNotFoundException(
          "Product with ID: ".concat(id.toString()).concat(" Not Found"));
    }
    return product.get();
  }

  @Override
  public void disable(UUID id) {
    ProductEntity changes = new ProductEntity();
    changes.setDisabled(true);
    this.update(id, changes);
  }

  @Override
  public void enable(UUID id) {
    ProductEntity changes = new ProductEntity();
    changes.setDisabled(false);
    this.update(id, changes);
  }

  @Override
  public void create(ProductEntity product) {
    productRepository.save(product);
  }

  @Override
  public void update(UUID id, ProductEntity changes) {
    ProductEntity oldProduct = this.getById(id);
    ProductEntity productWithChanges =
        ObjectUtils.getInstanceWithNotNullFields(changes, oldProduct, ProductEntity.class);
    productWithChanges.setId(id);
    productWithChanges.setCreatedAt(oldProduct.getCreatedAt());
    productRepository.save(productWithChanges);
  }
}
