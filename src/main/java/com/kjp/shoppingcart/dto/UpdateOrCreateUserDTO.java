package com.kjp.shoppingcart.dto;

import lombok.Builder;

@Builder
public record UpdateOrCreateUserDTO(
    String username, String email, String firstName, String lastName, String password) {}
