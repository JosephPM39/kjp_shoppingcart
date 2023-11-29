package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.CredentialsDTO;
import com.kjp.shoppingcart.dto.TokenDTO;
import com.kjp.shoppingcart.services.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/sign-in")
    public TokenDTO login(@RequestBody CredentialsDTO credentials) {
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
