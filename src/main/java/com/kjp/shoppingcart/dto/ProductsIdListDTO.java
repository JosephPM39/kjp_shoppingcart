package com.kjp.shoppingcart.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ProductsIdListDTO(@NotBlank UUID[] productsId) {}
