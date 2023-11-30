package com.kjp.shoppingcart.specifications;

import com.kjp.shoppingcart.entities.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification {
   public static <E extends BaseEntity> Specification<E> fieldEquals(String field, Object value) {
       return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));
   };
}
