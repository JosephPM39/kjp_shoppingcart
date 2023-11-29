package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.CredentialsDTO;
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
    public String login(@RequestBody CredentialsDTO credentials) {
        return authService.signIn(credentials);
    }
}
