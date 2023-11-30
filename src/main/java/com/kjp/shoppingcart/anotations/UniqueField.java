package com.kjp.shoppingcart.anotations;

import com.kjp.shoppingcart.entities.BaseEntity;
import com.kjp.shoppingcart.repositories.IBaseRepository;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.UUID;

import java.lang.annotation.*;

@Documented
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = UniqueFieldValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueField {
    String message() default "Must be unique";
    String fieldName();

    Class<? extends IBaseRepository<?, ?>> repository();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        UniqueField[] value();
    }
}
