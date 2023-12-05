package com.kjp.shoppingcart.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiEnvConfig {
  @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
  private String KEYCLOAK_CLIENT_ID;

  @Value("${app.auth-client.client-secret}")
  private String KEYCLOAK_CLIENT_SECRET;

  @Value("${spring.security.oauth2.resourceserver.jwt.generate-token}")
  private String KEYCLOAK_SERVER_GENERATE_TOKEN_URL;

  @Value("${spring.security.oauth2.client.provider.keycloak.user-name-attribute}")
  private String KEYCLOAK_JWT_PRINCIPAL_ATTRIBUTE;

  @Value(value = "${app.auth-client.server-url}")
  private String KEYCLOAK_SERVER_URL;

  @Value(value = "${app.auth-client.realm-name}")
  private String KEYCLOAK_REALM_NAME;

  @Value(value = "${app.auth-client.realm-master}")
  private String KEYCLOAK_MASTER_REALM_NAME;

  @Value(value = "${app.auth-client.admin-cli}")
  private String KEYCLOAK_CLI_NAME;

  @Value(value = "${app.auth-client.user-console}")
  private String KEYCLOAK_CONSOLE_USER;

  @Value(value = "${app.auth-client.password-console}")
  private String KEYCLOAK_CONSOLE_PASSWORD;
}
