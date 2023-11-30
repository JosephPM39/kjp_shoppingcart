package com.kjp.shoppingcart.config;

import com.kjp.shoppingcart.anotations.UniqueFieldValidator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public UniqueFieldValidator uniqueFieldValidator() {
        return new UniqueFieldValidator();
    }
}
