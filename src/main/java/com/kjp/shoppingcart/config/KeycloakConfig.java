package com.kjp.shoppingcart.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

  @Value(value = "${app.auth-client.server-url}")
  private String SERVER_URL;
  @Value(value = "${app.auth-client.realm-name}")
  private String REALM_NAME;
  @Value(value = "${app.auth-client.realm-master}")
  private String REALM_MASTER;
  @Value(value = "${app.auth-client.admin-cli}")
  private String ADMIN_CLI;
  @Value(value = "${app.auth-client.user-console}")
  private String USER_CONSOLE;
  @Value(value = "${app.auth-client.password-console}")
  private String PASSWORD_CONSOLE;
  @Value(value = "${app.auth-client.client-secret}")
  private String CLIENT_SECRET;

  public RealmResource getRealmResource() {
    Keycloak keycloak =
        KeycloakBuilder.builder()
            .serverUrl(SERVER_URL)
            .realm(REALM_MASTER)
            .clientId(ADMIN_CLI)
            .username(USER_CONSOLE)
            .password(PASSWORD_CONSOLE)
            .clientSecret(CLIENT_SECRET)
            .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
            .build();

    return keycloak.realm(REALM_NAME);
  }

  public UsersResource getUserResource() {
    RealmResource realmResource = getRealmResource();
    return realmResource.users();
  }
}
