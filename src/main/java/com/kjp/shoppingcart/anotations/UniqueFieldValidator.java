package com.kjp.shoppingcart.anotations;

import com.kjp.shoppingcart.entities.BaseEntity;
import com.kjp.shoppingcart.repositories.IBaseRepository;
import com.kjp.shoppingcart.repositories.ICategoryRepository;
import com.kjp.shoppingcart.services.IAuthService;
import com.kjp.shoppingcart.specifications.EntitySpecification;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@NoArgsConstructor
public class UniqueFieldValidator implements ConstraintValidator<UniqueField, Object>, BeanFactoryAware {

    private Class<? extends IBaseRepository<?, ?>> repositoryClass;

    private Class<?>[] groups;

    private String fieldName;

    private BeanFactory beanFactory;

    private IBaseRepository<?, ?> repository;

    @Override
    public void initialize(UniqueField constraintAnnotation) {
        this.groups = constraintAnnotation.groups();
        this.fieldName = constraintAnnotation.fieldName();
        this.repositoryClass = constraintAnnotation.repository();
        if (this.beanFactory == null) {
            return;
        }
        this.repository = this.beanFactory.getBean(repositoryClass);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (this.repository == null) {
            return true;
        }

        if (value == null) {
            return true;
        }

        boolean pass = !repository.existsByField(fieldName, value);

        System.out.println("VALUE: " + value.toString() + " FIELD NAME " + fieldName + " PASS " + pass);

        return pass;
    }
}
