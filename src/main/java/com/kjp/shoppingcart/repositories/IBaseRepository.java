package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.BaseEntity;
import com.kjp.shoppingcart.specifications.EntitySpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IBaseRepository<E extends BaseEntity, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    default boolean existsByField(String fieldName, Object value) {
        Specification<E> spec = Specification.where(EntitySpecification.<E>fieldEquals(fieldName, value));
        return exists(spec);
    }

}
