package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.UpdateOrCreateUserDTO;
import com.kjp.shoppingcart.dto.UserWithDefaultRoleDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import com.kjp.shoppingcart.services.IUserService;
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
    public List<UserEntity> getAllUsers() {
        return userService.findAllLocalUsers();
    }

    @GetMapping("/{username}")
    public UserEntity getUserByUsername(@PathVariable("username") String username) {
        return userService.searchLocalUserByUsername(username);
    }

    @PostMapping("/")
    public void createUser(@RequestBody UpdateOrCreateUserDTO dto) {
        userService.createUser(dto);
    }

    @DeleteMapping("/")
    public void deleteUser() {
        UUID userId = userService.getAuthenticatedUserKeycloakId();
        userService.deleteUser(userId);
    }

    @PatchMapping("/")
    public void updateUser(@RequestBody UpdateOrCreateUserDTO dto) {
        UUID userId = userService.getAuthenticatedUserKeycloakId();
        userService.updateUser(userId, dto);
    }

    @PostMapping("/{username}/ban")
    public void banUser(@PathVariable("username") String username) {
        userService.banUser(username);
    }

    @PostMapping("/{username}/quit-ban")
    public void quitBanUser(@PathVariable("username") String username) {
        userService.quitBanUser(username);
    }

    @PostMapping("/{username}/to-admin")
    public void toAdmin(@PathVariable("username") String username) {
        userService.addAdminRoleToUser(username);
    }

}
