package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.SignInCredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import com.kjp.shoppingcart.dto.UpdateOrCreateUserDTO;
import com.kjp.shoppingcart.services.IAuthService;
import com.kjp.shoppingcart.services.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class AuthController {

  private final IAuthService authService;
  private final IUserService userService;

  @Autowired
  public AuthController(IAuthService authService, IUserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping("/sign-in")
  public TokenDTO login(@Valid @RequestBody SignInCredentialsDTO credentials) {
    return authService.signIn(credentials);
  }

  @PostMapping("/sign-out")
  public String logout() {
    return "Logout successful";
  }

  @PostMapping("/sign-up")
  @PreAuthorize("permitAll()")
  public void signup(@RequestBody UpdateOrCreateUserDTO dto) {
    userService.createUser(dto);
  }

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome";
  }
}
