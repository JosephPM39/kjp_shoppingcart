package com.kjp.shoppingcart.entities;

import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products")
public class ProductEntity extends BaseEntity {

  @Size(max = 40, min = 1, groups = {CreateGroup.class, UpdateGroup.class})
  @NotBlank(groups = CreateGroup.class)
  @Column(length = 40, nullable = false)
  private String name;

  @Size(max = 40, min = 1, groups = {CreateGroup.class, UpdateGroup.class})
  @NotBlank(groups = CreateGroup.class)
  @Column(length = 40, nullable = false)
  private String brand;

  @DecimalMin(value = "0.00", groups = {CreateGroup.class, UpdateGroup.class})
  @Digits(integer = 5, fraction = 2, groups = {CreateGroup.class, UpdateGroup.class})
  @NotBlank(groups = CreateGroup.class)
  @Column(nullable = false)
  private BigDecimal price;


  @DecimalMin(value = "0.00", groups = {CreateGroup.class, UpdateGroup.class})
  @Digits(integer = 5, fraction = 2, groups = {CreateGroup.class, UpdateGroup.class})
  @NotBlank(groups = CreateGroup.class)
  @Column(name = "discount_offer")
  private BigDecimal discountOffer = new BigDecimal(0.00);

  @Size(max = 255, min = 1, groups = {CreateGroup.class, UpdateGroup.class})
  @NotBlank(groups = CreateGroup.class)
  @Column private String description;

  @Size(max = 255, min = 1, groups = {CreateGroup.class, UpdateGroup.class})
  @NotBlank(groups = CreateGroup.class)
  @Column private String code;

  @ManyToMany()
  @JoinTable(
      name = "products_categories",
      joinColumns =
          @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
          @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false))
  private List<CategoryEntity> categories;

  @Column private boolean disabled;
}
