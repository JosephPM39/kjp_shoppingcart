package com.kjp.shoppingcart.mappers;

import com.kjp.shoppingcart.dto.ProductCartDTO;
import com.kjp.shoppingcart.entities.ProductCartEntity;
import com.kjp.shoppingcart.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;

public class ProductCartMapper {

  public static ProductCartDTO getProductCartDTO(ProductCartEntity productCartEntity) {
    ProductEntity product = productCartEntity.getProduct();
    return ProductCartDTO.builder()
        .brand(product.getBrand())
        .code(product.getCode())
        .discountOffer(product.getDiscountOffer())
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .quantity(productCartEntity.getQuantity())
        .build();
  }

  public static List<ProductCartDTO> getProductCartDTOList(
      List<ProductCartEntity> productCartEntities) {
    List<ProductCartDTO> productCartDTOS = new ArrayList<>();
    for (ProductCartEntity productCartEntity : productCartEntities) {
      productCartDTOS.add(getProductCartDTO(productCartEntity));
    }
    return productCartDTOS;
  }
}
