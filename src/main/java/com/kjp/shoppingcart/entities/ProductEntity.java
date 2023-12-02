package com.kjp.shoppingcart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity(name = "products")
public class ProductEntity extends BaseEntity {

   @Column(length = 40, nullable = false)
   private String name;

   @ManyToOne()
   @JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = false)
   private BrandEntity brand;

   @Column(nullable = false)
   private BigDecimal price;

   @Column(name = "discount_offer")
   private BigDecimal discountOffer = new BigDecimal(0.00);

   @Column
   private String description;

   @Column
   private String code;

   @ManyToMany()
   @JoinTable(
      name = "products_categories",
      joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
   )
   private List<CategoryEntity> categories;

   @Column
   private boolean disabled;

}
