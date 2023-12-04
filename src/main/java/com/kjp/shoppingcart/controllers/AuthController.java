package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import com.kjp.shoppingcart.services.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Validated
public class AuthController {

  @Autowired private IAuthService authService;

  @PostMapping("/sign-in")
  public TokenDTO login(@Valid @RequestBody SignInCredentialsDTO credentials) {
    return authService.signIn(credentials);
  }

  @PostMapping("/sign-out")
  public String logout() {
    return "Logout successful";
  }

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome";
  }
}
