package com.kjp.shoppingcart.entities;

import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "categories")
public class CategoryEntity extends BaseEntity {
    @Size(max = 40, min = 1, groups = {CreateGroup.class, UpdateGroup.class})
    @NotBlank(groups = CreateGroup.class)
    @Column(length = 40, nullable = false, unique = true)
    private String name;

    @Size(max = 255, min = 0, groups = {CreateGroup.class, UpdateGroup.class})
    @Column
    private String description;

    @NotBlank(groups = CreateGroup.class)
    @Column(nullable = false)
    private boolean disabled = false;
}
