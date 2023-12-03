package com.kjp.shoppingcart.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReviewDTO(
        UUID productId,
        String title,
        String description
) {
}
