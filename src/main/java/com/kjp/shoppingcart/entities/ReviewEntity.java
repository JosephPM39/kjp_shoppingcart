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
@Entity(name = "reviews")
public class ReviewEntity extends BaseEntity {

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
      name = "user_id",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  private UserEntity user;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "product_id")
  private UUID productId;

  @Column(length = 40, nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;
}
