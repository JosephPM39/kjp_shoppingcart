package com.kjp.shoppingcart.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

  private final ApiEnvConfig apiEnvConfig;

  @Autowired
  public KeycloakConfig(ApiEnvConfig apiEnvConfig) {
    this.apiEnvConfig = apiEnvConfig;
  }

  public RealmResource getRealmResource() {
    Keycloak keycloak =
        KeycloakBuilder.builder()
            .serverUrl(apiEnvConfig.getKEYCLOAK_SERVER_URL())
            .realm(apiEnvConfig.getKEYCLOAK_MASTER_REALM_NAME())
            .clientId(apiEnvConfig.getKEYCLOAK_CLI_NAME())
            .username(apiEnvConfig.getKEYCLOAK_CONSOLE_USER())
            .password(apiEnvConfig.getKEYCLOAK_CONSOLE_PASSWORD())
            .clientSecret(apiEnvConfig.getKEYCLOAK_CLIENT_SECRET())
            .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
            .build();

    return keycloak.realm(apiEnvConfig.getKEYCLOAK_REALM_NAME());
  }

  public UsersResource getUserResource() {
    RealmResource realmResource = getRealmResource();
    return realmResource.users();
  }
}
