package com.kjp.shoppingcart.entities;

import com.kjp.shoppingcart.anotations.UniqueField;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "categories")
public class CategoryEntity extends BaseEntity {
    @NotBlank()
    @Size(max = 40, min = 1)

    @Column(length = 40, nullable = false, unique = true)
    @UniqueField(fieldName = "name", repository = ICategoryRepository.class)
    private String name;

    @Column
    private String description;
}
