package com.kjp.shoppingcart.dto;

import lombok.Builder;

@Builder
public record TokenDTO(
String accessToken,
String expiresIn,
String refreshExpiresIn,
String refreshToken,
String tokenType,
String notBeforePolicy,
String sessionState,
String scope
) {
}
