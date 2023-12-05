package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBaseRepository<E extends BaseEntity, ID>
    extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {}
