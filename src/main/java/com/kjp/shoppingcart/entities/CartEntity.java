package com.kjp.shoppingcart.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "carts")
public class CartEntity extends BaseEntity {

  @OneToOne
  @JoinColumn(
      name = "user_id",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  private UserEntity user;

  @Column(name = "user_id")
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CartStatusEnum status = CartStatusEnum.PENDING;

  @ManyToMany
  @JoinTable(
      name = "products_carts",
      joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
          @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false))
  private List<ProductEntity> products;
}
