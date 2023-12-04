package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "users")
public class UserEntity extends BaseEntity {

  @Column(name = "keycloak_id", unique = true, nullable = false)
  private UUID keycloakId;

  @Column(length = 80, unique = true, nullable = false)
  private String email;

  @Column(length = 40, nullable = false)
  private String username;

  @Column(name = "first_name", length = 40, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = 40, nullable = false)
  private String lastName;

  @Column private boolean banned;
}
