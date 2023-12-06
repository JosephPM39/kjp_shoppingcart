package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kjp.shoppingcart.config.ApiEnvConfig;
import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import com.kjp.shoppingcart.fakers.TokenFaker;
import java.net.URI;
import java.net.URISyntaxException;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class AuthServiceTest {

  private final RestTemplate restTemplateMock = mock(RestTemplate.class);
  private final ApiEnvConfig apiEnvConfigMock = mock(ApiEnvConfig.class);

  private final AuthService authService = new AuthService(restTemplateMock, apiEnvConfigMock);

  @Test
  void signIn() throws URISyntaxException {

    JSONObject jsonObject =
        (JSONObject) JSONValue.parse(TokenFaker.getStringJsonKeyClaokResponse());
    ResponseEntity<JSONObject> response = ResponseEntity.ok(jsonObject);

    when(restTemplateMock.postForEntity(any(URI.class), any(Object.class), eq(JSONObject.class)))
        .thenReturn(response);
    when(apiEnvConfigMock.getKEYCLOAK_SERVER_GENERATE_TOKEN_URL())
        .thenReturn("http://localhost:8081/realm");

    TokenDTO token = authService.signIn(new SignInCredentialsDTO("test.admin", "testtest"));
    assertEquals("Bearer", token.tokenType());
    assertEquals(300, token.expiresIn());
    assertEquals(1800, token.refreshExpiresIn());
    verify(restTemplateMock).postForEntity(any(URI.class), any(Object.class), eq(JSONObject.class));
    verify(apiEnvConfigMock).getKEYCLOAK_SERVER_GENERATE_TOKEN_URL();
  }
}
