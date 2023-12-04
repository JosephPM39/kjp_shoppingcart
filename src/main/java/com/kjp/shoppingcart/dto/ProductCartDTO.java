package com.kjp.shoppingcart.dto;

import com.kjp.shoppingcart.entities.BrandEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProductCartDTO(
    UUID id,
    String name,
    BrandEntity brand,
    BigDecimal price,
    BigDecimal discountOffer,
    String code,
    Integer quantity) {}
