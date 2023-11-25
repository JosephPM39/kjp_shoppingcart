package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "categories")
public class Categories extends BaseEntity {
    @Column(length = 40)
    private String name;

    @Column
    private String description;
}
