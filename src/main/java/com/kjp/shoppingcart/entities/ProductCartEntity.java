package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products_carts")
public class ProductCartEntity extends BaseEntity {

  @Column(name = "product_id")
  private UUID productId;

  @Column(name = "cart_id")
  private UUID cartId;

  @Column(nullable = false)
  private Integer quantity = 1;

  @ManyToOne
  @JoinColumn(
      name = "product_id",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  private ProductEntity product;

  @ManyToOne
  @JoinColumn(
      name = "cart_id",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  private CartEntity cart;
}
