package com.kjp.shoppingcart.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {
  @ManyToOne
  @JoinColumn(
      name = "user_id",
      referencedColumnName = "id",
      nullable = false,
      updatable = false,
      insertable = false)
  private UserEntity user;

  @Column(name = "user_id")
  private UUID userId;

  @Column(nullable = false)
  private BigDecimal total;

  @Enumerated(EnumType.STRING)
  @Column
  private OrderStatusEnum status = OrderStatusEnum.PENDING;
}
