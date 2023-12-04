package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.GetUserDTO;
import com.kjp.shoppingcart.dto.UpdateOrCreateUserDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import java.util.List;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;

public interface IUserService {
  public List<GetUserDTO> findAllUsers();

  public UserEntity findLocalUserByKeycloakId(UUID keycloakId);

  public List<UserEntity> findAllLocalUsers();

  public UserRepresentation searchUserByUsername(String username);

  public UserEntity findUserByKeycloakId(UUID keycloakId);

  public UUID getAuthenticatedUserKeycloakId();

  public UUID getAuthenticatedLocalUserId();

  public UserEntity searchLocalUserByUsername(String username);

  public void createUser(UpdateOrCreateUserDTO userDTO);

  public void deleteUser(UUID keycloakId);

  public void banUser(String username);

  public void quitBanUser(String username);

  public void updateUser(UUID userId, UpdateOrCreateUserDTO userDTO);

  public void addAdminRoleToUser(String username);
}
