package com.kjp.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kjp.shoppingcart.controllers")
@ComponentScan(basePackages = "com.kjp.shoppingcart.services")
@ComponentScan(basePackages = "com.kjp.shoppingcart.repositories")
@ComponentScan(basePackages = "com.kjp.shoppingcart.config")
@ComponentScan(basePackages = "com.kjp.shoppingcart.anotations")
public class ShoppingcartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartApplication.class, args);
	}

}
