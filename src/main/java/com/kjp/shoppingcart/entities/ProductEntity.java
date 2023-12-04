package com.kjp.shoppingcart.entities;

import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "products")
@NoArgsConstructor
public class ProductEntity extends BaseEntity {

  public ProductEntity(String name, String brand, BigDecimal price, BigDecimal discountOffer, String description, String code, List<CategoryEntity> categories, boolean disabled) {
    this.name = name;
    this.brand = brand;
    this.price = price;
    this.discountOffer = discountOffer;
    this.description = description;
    this.code = code;
    this.categories = categories.stream().toList();
    this.disabled = disabled;
  }

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
  @NotNull(groups = CreateGroup.class)
  @Column(nullable = false)
  private BigDecimal price;


  @DecimalMin(value = "0.00", groups = {CreateGroup.class, UpdateGroup.class})
  @Digits(integer = 5, fraction = 2, groups = {CreateGroup.class, UpdateGroup.class})
  @NotNull(groups = CreateGroup.class)
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
          @JoinColumn(name = "product_id"),
      inverseJoinColumns =
          @JoinColumn(name = "category_id"))
  private List<CategoryEntity> categories;

  @Column private boolean disabled;
}
