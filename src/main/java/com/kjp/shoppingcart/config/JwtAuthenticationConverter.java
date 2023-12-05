package com.kjp.shoppingcart.config;

import java.util.*;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
      new JwtGrantedAuthoritiesConverter();
  private ApiEnvConfig apiEnvConfig;

  @Autowired
  public JwtAuthenticationConverter(ApiEnvConfig apiEnvConfig) {
    this.apiEnvConfig = apiEnvConfig;
  }

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    Collection<GrantedAuthority> authorities =
        Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream())
            .toList();
    return new JwtAuthenticationToken(jwt, authorities, getPrincipalName(jwt));
  }

  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
    Map<String, Object> resourceAccess;
    Map<String, Object> resources;
    Collection<String> resourceRoles;

    if (jwt.getClaim("resource_access") == null) {
      return List.of();
    }

    resourceAccess = jwt.getClaim("resource_access");

    if (resourceAccess.get(apiEnvConfig.getKEYCLOAK_CLIENT_ID()) == null) {
      return List.of();
    }

    resources = (Map<String, Object>) resourceAccess.get(apiEnvConfig.getKEYCLOAK_CLIENT_ID());

    if (resources.get("roles") == null) {
      return List.of();
    }

    resourceRoles = (Collection<String>) resources.get("roles");

    return resourceRoles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
        .toList();
  }

  private String getPrincipalName(Jwt jwt) {
    String claimName = JwtClaimNames.SUB;

    if (apiEnvConfig.getKEYCLOAK_JWT_PRINCIPAL_ATTRIBUTE() != null) {
      claimName = apiEnvConfig.getKEYCLOAK_JWT_PRINCIPAL_ATTRIBUTE();
    }

    return jwt.getClaim(claimName);
  }
}
