package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.config.KeycloakConfig;
import com.kjp.shoppingcart.dto.CreateUserDTO;
import com.kjp.shoppingcart.dto.GetUserDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.mappers.UserMapper;
import com.kjp.shoppingcart.repositories.IUserRepository;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements IUserService {

    IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<GetUserDTO> findAllUsers(){
        List<UserRepresentation> users = KeycloakConfig.getRealmResource().users().list();
        return UserMapper.toGetUserDTO(users);
    }

    @Override
    public UserEntity findLocalUserByKeycloakId(UUID keycloakId) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findFirstByKeycloakIdEquals(keycloakId);
        if (optionalUserEntity.isPresent()) {
            return optionalUserEntity.get();
        }
        throw new ResourceNotFoundException("User not found with the keycloak id:".concat(keycloakId.toString()));
    }

    @Override
    public List<UserEntity> findAllLocalUsers() {
        return null;
    }

    @Override
    public UserRepresentation searchUserByUsername(String username) {
        List<UserRepresentation> users = KeycloakConfig.getRealmResource().users().searchByUsername(username, true);
        Optional<UserRepresentation> user = users.stream().filter(userRepresentation -> {
            return userRepresentation.getUsername().equals(username);
        }).findFirst();
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found with the username: ".concat(username));
        }
        return user.get();
    }

    @Override
    public UserEntity findUserByKeycloakId(UUID keycloakId) {
        return null;
    }

    @Override
    public UUID getAuthenticatedUserKeycloakId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return UUID.fromString(userId);
    }

    public UserRepresentation findUserById(UUID userId) {
        return KeycloakConfig.getRealmResource().users().get(userId.toString()).toRepresentation();
    }

    @Override
    public void createUser(@NonNull CreateUserDTO userDTO) {

        int status = 0;
        UsersResource usersResource = KeycloakConfig.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakConfig.getRealmResource();

            List<RoleRepresentation> rolesRepresentation = null;

            if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
                rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
            } else {
                rolesRepresentation = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.getRoles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

            createLocalUser(userDTO.getUsername());

        } else if (status == 409) {
            log.error("User exist already!");
            throw new ResourceAlreadyExistsException("User already exists");
        } else {
            log.error("Error creating user, please contact with the administrator.");
            throw new InternalServerErrorException("Error crating user, please contact with the administrator.");
        }
    }

    private void createLocalUser(String userName) {
        UserRepresentation user = searchUserByUsername(userName);
        UserEntity userEntity = UserMapper.toUserEntity(user, false);
        this.userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(UUID userId){
        KeycloakConfig.getUserResource()
                .get(userId.toString())
                .remove();
    }

    @Override
    public void updateUser(UUID userId, @NonNull CreateUserDTO userDTO){

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource usersResource = KeycloakConfig.getUserResource().get(userId.toString());
        usersResource.update(user);
    }
}
