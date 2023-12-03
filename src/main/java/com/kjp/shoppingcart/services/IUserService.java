package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;

import java.util.List;

public interface IUserService {
    public List<UserRepresentation> findAllUsers();
    public List<UserRepresentation> searchUserByUsername(String username);
    public String createUser(@NonNull UserDTO userDTO);
    public void deleteUser(String userId);
    public void updateUser(String userId, @NonNull UserDTO userDTO);
}
