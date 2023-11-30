package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

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

}
