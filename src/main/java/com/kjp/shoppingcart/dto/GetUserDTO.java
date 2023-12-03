package com.kjp.shoppingcart.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record GetUserDTO(
        String username,
        String email,
        String firstName,
        String lastName,
        Set<String> roles
) {
}
