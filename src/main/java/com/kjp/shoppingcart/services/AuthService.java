package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.CredentialsDTO;
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

    @Override
    public String signIn(CredentialsDTO credentials) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("client_id", resourceId);
        body.add("grant_type", "password");
        body.add("client_secret", resourceSecret);
        body.add("username", credentials.userName());
        body.add("password", credentials.password());

        System.out.println(body.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);

        return response.toString();
    }
}
