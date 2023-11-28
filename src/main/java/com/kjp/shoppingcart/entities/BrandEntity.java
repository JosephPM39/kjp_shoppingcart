package com.kjp.shoppingcart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "brands")
public class BrandEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
