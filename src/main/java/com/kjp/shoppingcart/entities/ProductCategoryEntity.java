package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "products_categories")
public class ProductCategoryEntity extends BaseEntity {

  @Column(name = "product_id")
  private UUID productId;

  @Column(name = "category_id")
  private UUID categoryId;

  @ManyToOne
  @JoinColumn(
      name = "product_id",
      nullable = false,
      insertable = false,
      updatable = false)
  private ProductEntity product;

  @ManyToOne
  @JoinColumn(
      name = "category_id",
      nullable = false,
      insertable = false,
      updatable = false)
  private CategoryEntity category;
}
