package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.UpdateOrCreateUserDTO;
import com.kjp.shoppingcart.dto.UserWithDefaultRoleDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import com.kjp.shoppingcart.services.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
    public List<UserEntity> getAllUsers() {
        return userService.findAllLocalUsers();
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
    public UserEntity getUserByUsername(@PathVariable("username") String username) {
        return userService.searchLocalUserByUsername(username);
    }

    @PostMapping("/")
    @PreAuthorize("permitAll()")
    public void createUser(@RequestBody UpdateOrCreateUserDTO dto) {
        userService.createUser(dto);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
    public void deleteUser() {
        UUID userId = userService.getAuthenticatedUserKeycloakId();
        userService.deleteUser(userId);
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
    public void updateUser(@RequestBody UpdateOrCreateUserDTO dto) {
        UUID userId = userService.getAuthenticatedUserKeycloakId();
        userService.updateUser(userId, dto);
    }

    @PostMapping("/{username}/ban")
    @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
    public void banUser(@PathVariable("username") String username) {
        userService.banUser(username);
    }

    @PostMapping("/{username}/quit-ban")
    @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
    public void quitBanUser(@PathVariable("username") String username) {
        userService.quitBanUser(username);
    }

    @PostMapping("/{username}/to-admin")
    @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-admin'))")
    public void toAdmin(@PathVariable("username") String username) {
        userService.addAdminRoleToUser(username);
    }

}
