package com.kjp.shoppingcart.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public String hello(){
        return "Hello World";
    }

    @GetMapping("/admin-or-user")
    @PreAuthorize("hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
    public String helloAuth() {
        return "Hello admin or user";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
    public String helloAdmin(){
        return "Hello Admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
    public String helloUser(){
        return "Hello User";
    }
}
