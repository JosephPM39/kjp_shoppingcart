package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBaseRepository<E extends BaseEntity, T> extends JpaRepository<E, T> {
}
