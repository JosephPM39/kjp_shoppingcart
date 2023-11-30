package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthService implements IAuthService {

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Value("${resource-secret}")
    private String resourceSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.generate-token}")
    private String loginUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TokenDTO signIn(SignInCredentialsDTO credentials) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("client_id", resourceId);
        body.add("grant_type", "password");
        body.add("client_secret", resourceSecret);
        body.add("username", credentials.userName());
        body.add("password", credentials.password());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);

            ResponseEntity<JSONObject> response = restTemplate.postForEntity(loginUrl, request, JSONObject.class);

            JSONObject bodyResponse = response.getBody();

            return TokenDTO.builder()
                    .accessToken(bodyResponse.get("access_token").toString())
                    .refreshToken(bodyResponse.get("refresh_token").toString())
                    .expiresIn(bodyResponse.get("expires_in").toString())
                    .refreshExpiresIn(bodyResponse.get("refresh_expires_in").toString())
                    .scope(bodyResponse.get("scope").toString())
                    .tokenType(bodyResponse.get("token_type").toString())
                    .notBeforePolicy(bodyResponse.get("not-before-policy").toString())
                    .sessionState(bodyResponse.get("session_state").toString())
                    .build();
    }

}
