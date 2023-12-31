package com.kjp.shoppingcart.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProductCartDTO(
    UUID id,
    String name,
    String brand,
    BigDecimal price,
    BigDecimal discountOffer,
    String code,
    Integer quantity) {}
