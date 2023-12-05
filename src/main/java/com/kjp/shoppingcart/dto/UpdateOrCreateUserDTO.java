package com.kjp.shoppingcart.dto;

import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateOrCreateUserDTO(
    @NotBlank(groups = CreateGroup.class)
        @Size(
            max = 40,
            min = 1,
            groups = {CreateGroup.class, UpdateGroup.class})
        String username,
    @NotBlank(groups = CreateGroup.class)
        @Size(
            max = 80,
            min = 5,
            groups = {CreateGroup.class, UpdateGroup.class})
        String email,
    @NotBlank(groups = CreateGroup.class)
        @Size(
            max = 40,
            min = 1,
            groups = {CreateGroup.class, UpdateGroup.class})
        String firstName,
    @NotBlank(groups = CreateGroup.class)
        @Size(
            max = 40,
            min = 1,
            groups = {CreateGroup.class, UpdateGroup.class})
        String lastName,
    @NotBlank(groups = CreateGroup.class)
        @Size(
            max = 40,
            min = 35,
            groups = {CreateGroup.class, UpdateGroup.class})
        String password) {}
