package com.kjp.shoppingcart.dto;

import java.util.Set;
import lombok.Builder;

@Builder
public record UserWithDefaultRoleDTO(
    String username,
    String email,
    String firstName,
    String lastName,
    String password,
    Set<String> roles) {
  public UserWithDefaultRoleDTO {
    if (roles == null || roles.isEmpty()) {
      roles = Set.of("user");
    }
  }
}
