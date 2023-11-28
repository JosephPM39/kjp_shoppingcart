package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "categories")
public class CategoryEntity extends BaseEntity {
    @Column(length = 40, nullable = false)
    private String name;

    @Column
    private String description;
}
