package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.config.KeycloakConfig;
import com.kjp.shoppingcart.dto.GetUserDTO;
import com.kjp.shoppingcart.dto.UpdateOrCreateUserDTO;
import com.kjp.shoppingcart.dto.UserWithDefaultRoleDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.mappers.UserMapper;
import com.kjp.shoppingcart.repositories.IUserRepository;
import com.kjp.shoppingcart.utils.ObjectUtils;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService implements IUserService {

  IUserRepository userRepository;
  KeycloakConfig keycloakConfig;

  @Autowired
  public UserService(IUserRepository userRepository, KeycloakConfig keycloakConfig) {
    this.userRepository = userRepository;
    this.keycloakConfig = keycloakConfig;
  }

  @Override
  public List<GetUserDTO> findAllUsers() {
    List<UserRepresentation> users = keycloakConfig.getRealmResource().users().list();
    return UserMapper.toGetUserDTO(users);
  }

  @Override
  public UserEntity findLocalUserByKeycloakId(UUID keycloakId) {
    Optional<UserEntity> optionalUserEntity =
        this.userRepository.findFirstByKeycloakIdEquals(keycloakId);
    if (optionalUserEntity.isPresent()) {
      return optionalUserEntity.get();
    }
    throw new ResourceNotFoundException(
        "User not found with the keycloak id:".concat(keycloakId.toString()));
  }

  @Override
  public List<UserEntity> findAllLocalUsers() {
    return this.userRepository.findAll();
  }

  @Override
  public UserRepresentation searchUserByUsername(String username) {
    List<UserRepresentation> users =
        keycloakConfig.getRealmResource().users().searchByUsername(username, true);
    Optional<UserRepresentation> user =
        users.stream()
            .filter(
                userRepresentation -> {
                  return userRepresentation.getUsername().equals(username);
                })
            .findFirst();
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("User not found with the username: ".concat(username));
    }
    return user.get();
  }

  public UserEntity findOrCreateUserByKeycloakId(UUID keycloakId) {
    Optional<UserEntity> user = this.userRepository.findFirstByKeycloakIdEquals(keycloakId);
    if (user.isPresent()) {
      return user.get();
    }
    String username = getAuthenticatedUserKeycloakUsername();
    createLocalUser(username);
    user = this.userRepository.findFirstByKeycloakIdEquals(keycloakId);
    if (user.isPresent()) {
      return user.get();
    }
    throw new InternalServerErrorException("Server could no find or create the local user");
  }

  @Override
  public UUID getAuthenticatedUserKeycloakId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
      String userId = jwtAuthenticationToken.getToken().getSubject();
      return UUID.fromString(userId);
    }

    return null;
  }

  private String getAuthenticatedUserKeycloakUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
      return jwtAuthenticationToken.getToken().getClaim("preferred_username");
    }

    return null;
  }

  @Override
  public UUID getAuthenticatedLocalUserId() {
    UserEntity user = findOrCreateUserByKeycloakId(getAuthenticatedUserKeycloakId());
    return user.getId();
  }

  @Override
  public UserEntity searchLocalUserByUsername(String username) {
    Optional<UserEntity> user = this.userRepository.findFirstByUsernameEquals(username);
    if (user.isPresent()) {
      return user.get();
    }
    throw new ResourceNotFoundException("No user exists with the username: ".concat(username));
  }

  @Override
  public void createUser(@NonNull UpdateOrCreateUserDTO dto) {

    UserWithDefaultRoleDTO userDTO = UserMapper.toUserWithDefaultRoleDTO(dto);

    int status = 0;
    UsersResource usersResource = keycloakConfig.getUserResource();

    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setFirstName(userDTO.firstName());
    userRepresentation.setLastName(userDTO.lastName());
    userRepresentation.setEmail(userDTO.email());
    userRepresentation.setUsername(userDTO.username());
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
      credentialRepresentation.setValue(userDTO.password());

      usersResource.get(userId).resetPassword(credentialRepresentation);

      RealmResource realmResource = keycloakConfig.getRealmResource();

      List<RoleRepresentation> rolesRepresentation = null;

      if (userDTO.roles() == null || userDTO.roles().isEmpty()) {
        rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
      } else {
        rolesRepresentation =
            realmResource.roles().list().stream()
                .filter(
                    role ->
                        userDTO.roles().stream()
                            .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                .toList();
      }

      realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

      createLocalUser(userDTO.username());

    } else if (status == 409) {
      log.error("User exist already!");
      throw new ResourceAlreadyExistsException("User already exists");
    } else {
      log.error("Error creating user, please contact with the administrator.");
      throw new InternalServerErrorException(
          "Error crating user, please contact with the administrator.");
    }
  }

  private void createLocalUser(String userName) {
    UserRepresentation user = searchUserByUsername(userName);
    UserEntity userEntity = UserMapper.toUserEntity(user, false);
    this.userRepository.save(userEntity);
  }

  @Override
  public void deleteUser(UUID keycloakId) {
    keycloakConfig.getUserResource().get(keycloakId.toString()).remove();
    deleteLocalUser(keycloakId);
  }

  private void deleteLocalUser(UUID userKeycloakId) {
    this.userRepository.deleteByKeycloakIdEquals(userKeycloakId);
  }

  @Override
  public void banUser(String username) {
    UserEntity user = searchLocalUserByUsername(username);
    user.setBanned(true);
    this.userRepository.save(user);
  }

  @Override
  public void quitBanUser(String username) {
    UserEntity user = searchLocalUserByUsername(username);
    user.setBanned(false);
    this.userRepository.save(user);
  }

  @Override
  public void updateUser(UUID userKeycloakId, @NonNull UpdateOrCreateUserDTO userDTO) {

    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(false);
    credentialRepresentation.setType(OAuth2Constants.PASSWORD);
    credentialRepresentation.setValue(userDTO.password());

    UserRepresentation user = new UserRepresentation();
    user.setUsername(userDTO.username());
    user.setFirstName(userDTO.firstName());
    user.setLastName(userDTO.lastName());
    user.setEmail(userDTO.email());
    user.setEnabled(true);
    user.setEmailVerified(true);
    user.setCredentials(Collections.singletonList(credentialRepresentation));

    UserResource usersResource = keycloakConfig.getUserResource().get(userKeycloakId.toString());
    usersResource.update(user);

    updateLocalUser(userKeycloakId, userDTO);
  }

  private void updateLocalUser(UUID userKeycloakId, UpdateOrCreateUserDTO userDTO) {
    Optional<UserEntity> optionalUserEntity =
        this.userRepository.findFirstByKeycloakIdEquals(userKeycloakId);
    if (optionalUserEntity.isEmpty()) {
      throw new ResourceNotFoundException(
          "The local user not found with the keycloakId: ".concat(userKeycloakId.toString()));
    }
    UserEntity changes = UserMapper.toUserEntity(userDTO);
    UserEntity oldUser = optionalUserEntity.get();
    UserEntity newUser =
        ObjectUtils.getInstanceWithNotNullFields(changes, oldUser, UserEntity.class);
    newUser.setId(oldUser.getId());
    newUser.setCreatedAt(oldUser.getCreatedAt());
    this.userRepository.save(newUser);
  }

  @Override
  public void addAdminRoleToUser(String username) {
    RealmResource realmResource = keycloakConfig.getRealmResource();
    UserRepresentation user = searchUserByUsername(username);
    List<RoleRepresentation> role = List.of(realmResource.roles().get("admin").toRepresentation());
    realmResource.users().get(user.getId()).roles().realmLevel().add(role);
  }
}
