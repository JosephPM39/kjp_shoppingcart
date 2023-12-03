package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "orders_products")
public class OrderProductEntity extends BaseEntity {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "order_id")
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private OrderEntity order;

    @Column(name = "sold_price", nullable = false)
    private BigDecimal soldPrice;


}
