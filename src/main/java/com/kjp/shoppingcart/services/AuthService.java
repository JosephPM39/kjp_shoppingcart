package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.config.ApiEnvConfig;
import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import jakarta.ws.rs.InternalServerErrorException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService implements IAuthService {

  private final RestTemplate restTemplate;
  private final ApiEnvConfig apiEnvConfig;

  @Autowired
  public AuthService(RestTemplate restTemplate, ApiEnvConfig apiEnvConfig) {
    this.restTemplate = restTemplate;
    this.apiEnvConfig = apiEnvConfig;
  }

  @Override
  public TokenDTO signIn(SignInCredentialsDTO credentials) {

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
    body.add("client_id", apiEnvConfig.getKEYCLOAK_CLIENT_ID());
    body.add("grant_type", "password");
    body.add("client_secret", apiEnvConfig.getKEYCLOAK_CLIENT_SECRET());
    body.add("username", credentials.userName());
    body.add("password", credentials.password());

    HttpEntity<MultiValueMap<String, String>> request =
        new HttpEntity<MultiValueMap<String, String>>(body, headers);

    ResponseEntity<JSONObject> response;

    try {
      response =
          restTemplate.postForEntity(
              new URI(apiEnvConfig.getKEYCLOAK_SERVER_GENERATE_TOKEN_URL()),
              request,
              JSONObject.class);
    } catch (URISyntaxException e) {
      throw new InternalServerErrorException("Some problem with the signIn method uri");
    }

    JSONObject bodyResponse = response.getBody();

    if (bodyResponse == null) {
      throw new InternalServerErrorException("No body response in signIn with keycloak request");
    }

    return TokenDTO.builder()
        .accessToken(bodyResponse.get("access_token").toString())
        .refreshToken(bodyResponse.get("refresh_token").toString())
        .expiresIn(Integer.parseInt(bodyResponse.get("expires_in").toString()))
        .refreshExpiresIn(Integer.parseInt(bodyResponse.get("refresh_expires_in").toString()))
        .scope(bodyResponse.get("scope").toString())
        .tokenType(bodyResponse.get("token_type").toString())
        .notBeforePolicy(bodyResponse.get("not-before-policy").toString())
        .sessionState(bodyResponse.get("session_state").toString())
        .build();
  }
}
