package com.kjp.shoppingcart.dto;

import lombok.Builder;

@Builder
public record TokenDTO(
    String accessToken,
    Integer expiresIn,
    Integer refreshExpiresIn,
    String refreshToken,
    String tokenType,
    String notBeforePolicy,
    String sessionState,
    String scope) {}
