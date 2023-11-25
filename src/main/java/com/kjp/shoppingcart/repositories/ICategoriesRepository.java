package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICategoriesRepository extends JpaRepository<Categories, UUID> {

}
