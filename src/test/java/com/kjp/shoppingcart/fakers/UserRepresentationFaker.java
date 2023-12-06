package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;

public class UserRepresentationFaker {
  public static UserRepresentation getFake() {
    Faker faker = new Faker();
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setId(UUID.randomUUID().toString());
    userRepresentation.setEmailVerified(true);
    userRepresentation.setEmail(faker.internet().emailAddress());
    userRepresentation.setUsername(faker.name().username());
    userRepresentation.setFirstName(faker.name().firstName());
    userRepresentation.setLastName(faker.name().lastName());
    userRepresentation.setClientRoles(new HashMap<>());
    return userRepresentation;
  }
}
