package com.kjp.shoppingcart.dto;

import java.util.Set;
import lombok.Builder;

@Builder
public record GetUserDTO(
    String username, String email, String firstName, String lastName, Set<String> roles) {}
