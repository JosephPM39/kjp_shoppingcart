package com.kjp.shoppingcart.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

@Builder
public record UserWithDefaultRoleDTO (
    String username,
    String email,
    String firstName,
    String lastName,
    String password,
    Set<String> roles
){
    public UserWithDefaultRoleDTO {
        if (roles.isEmpty()) {
            roles = Set.of("user");
        }
    }
}