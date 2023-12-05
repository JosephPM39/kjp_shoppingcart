package com.kjp.shoppingcart.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ReviewDTO(UUID productId, String title, String description) {}
