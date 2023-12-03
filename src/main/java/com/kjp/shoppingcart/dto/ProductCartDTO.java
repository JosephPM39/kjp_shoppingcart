package com.kjp.shoppingcart.dto;

import com.kjp.shoppingcart.entities.BrandEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductCartDTO(
    UUID id,
    String name,
    BrandEntity brand,
    BigDecimal price,
    BigDecimal discountOffer,
    String code,
    Integer quantity
) {
}
