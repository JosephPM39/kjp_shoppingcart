package com.kjp.shoppingcart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInCredentialsDTO(
        @NotBlank(message = "Se requiere de un nombre de usuario")
        @Size(max = 40, min = 1, message = "El nombre de usuario debe ser mayor a 1 y menor a 41 caracteres")
        String userName,
        @NotBlank(message = "Se requiere de una contraseña")
        @Size(max = 35, message = "La contraseña debe ser menor a 36 caracteres")
        String password
){}
