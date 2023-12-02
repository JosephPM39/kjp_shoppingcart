package com.kjp.shoppingcart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInCredentialsDTO(
        @NotBlank()
        @Size(max = 40, min = 1)
        String userName,
        @NotBlank()
        @Size(max = 35)
        String password
){}
