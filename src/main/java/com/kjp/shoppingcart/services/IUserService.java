package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.CreateUserDTO;
import com.kjp.shoppingcart.dto.GetUserDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    public List<GetUserDTO> findAllUsers();
    public UserEntity findLocalUserByKeycloakId(UUID keycloakId);
    public List<UserEntity> findAllLocalUsers();
    public UserRepresentation searchUserByUsername(String username);
    public UserEntity findUserByKeycloakId(UUID keycloakId);
    public UUID getAuthenticatedUserKeycloakId();
    public void createUser(CreateUserDTO userDTO);
    public void deleteUser(UUID userId);
    public void updateUser(UUID userId, CreateUserDTO userDTO);
}
