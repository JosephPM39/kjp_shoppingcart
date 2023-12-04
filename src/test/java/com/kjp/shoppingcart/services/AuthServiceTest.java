package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;

import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {

  @Autowired private AuthService authService;

  @Test
  void signIn() {
    TokenDTO token = authService.signIn(new SignInCredentialsDTO("test.admin", "testtest"));
    assertEquals("Bearer", token.tokenType());
    assertEquals(300, token.expiresIn());
    assertEquals(1800, token.refreshExpiresIn());
  }
}
