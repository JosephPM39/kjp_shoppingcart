package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kjp.shoppingcart.config.KeycloakConfig;
import com.kjp.shoppingcart.dto.GetUserDTO;
import com.kjp.shoppingcart.dto.UpdateOrCreateUserDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.fakers.TokenFaker;
import com.kjp.shoppingcart.fakers.UserEntityFaker;
import com.kjp.shoppingcart.fakers.UserRepresentationFaker;
import com.kjp.shoppingcart.repositories.IUserRepository;
import jakarta.ws.rs.core.Response;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class UserServiceTest {

  @MockBean IUserRepository userRepositoryMock;

  @MockBean KeycloakConfig keycloakConfigMock;

  @Autowired IUserService userService;

  UsersResource usersResourceMock = mock(UsersResource.class);
  RealmResource realmResourceMock = mock(RealmResource.class);

  @BeforeEach
  public void setUp() {
    SecurityContextHolder.setContext(
        new SecurityContext() {
          @Override
          public Authentication getAuthentication() {
            return TokenFaker.getJwtAuthToken();
          }

          @Override
          public void setAuthentication(Authentication authentication) {}
        });

    when(keycloakConfigMock.getRealmResource()).thenReturn(realmResourceMock);
    when(keycloakConfigMock.getUserResource()).thenReturn(usersResourceMock);
    when(realmResourceMock.users()).thenReturn(usersResourceMock);
  }

  @Test
  void findAllUsers() {
    when(usersResourceMock.list()).thenReturn(List.of(UserRepresentationFaker.getFake()));

    List<GetUserDTO> user = userService.findAllUsers();
    assertEquals(1, user.size());

    verify(usersResourceMock).list();
  }

  @Test
  void findLocalUserByKeycloakId() {
    UserEntity userEntity = UserEntityFaker.getFake();
    when(userRepositoryMock.findFirstByKeycloakIdEquals(any(UUID.class)))
        .thenReturn(Optional.of(userEntity));

    UserEntity user = userService.findLocalUserByKeycloakId(userEntity.getKeycloakId());

    assertNotNull(user);
    assertEquals(userEntity.getId(), user.getId());

    verify(userRepositoryMock).findFirstByKeycloakIdEquals(any(UUID.class));
  }

  @Test
  void findAllLocalUsers() {
    when(userRepositoryMock.findAll()).thenReturn(new ArrayList<>());

    List<UserEntity> users = userService.findAllLocalUsers();
    assertNotNull(users);
    assertEquals(0, users.size());

    verify(userRepositoryMock).findAll();
  }

  @Test
  void searchUserByUsername() {
    UserRepresentation userRepresentation = UserRepresentationFaker.getFake();
    when(usersResourceMock.searchByUsername(any(String.class), eq(true)))
        .thenReturn(List.of(userRepresentation));

    UserRepresentation user = userService.searchUserByUsername(userRepresentation.getUsername());
    assertNotNull(user);

    verify(usersResourceMock).searchByUsername(any(String.class), eq(true));
  }

  @Test
  void searchUserByUsernameNotFound() {
    UserRepresentation userRepresentation = UserRepresentationFaker.getFake();
    when(usersResourceMock.searchByUsername(any(String.class), eq(true)))
        .thenReturn(List.of(userRepresentation));

    try {
      UserRepresentation user = userService.searchUserByUsername("test.user");
      assertNull(user);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(usersResourceMock).searchByUsername(any(String.class), eq(true));
  }

  @Test
  void getAuthenticatedUserKeycloakId() {
    UUID userId = userService.getAuthenticatedUserKeycloakId();
    assertNotNull(userId);
  }

  @Test
  void getAuthenticatedLocalUserId() {
    UserEntity userEntity = UserEntityFaker.getFake();
    when(userRepositoryMock.findFirstByKeycloakIdEquals(any(UUID.class)))
        .thenReturn(Optional.of(userEntity));
    UUID userId = userService.getAuthenticatedLocalUserId();
    assertNotNull(userId);
    verify(userRepositoryMock).findFirstByKeycloakIdEquals(any(UUID.class));
  }

  @Test
  void getAuthenticatedLocalUserIdNotCanCreate() {
    when(userRepositoryMock.findFirstByKeycloakIdEquals(any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      UUID userId = userService.getAuthenticatedLocalUserId();
      assertNull(userId);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(userRepositoryMock, times(1)).findFirstByKeycloakIdEquals(any(UUID.class));
  }

  @Test
  void searchLocalUserByUsername() {
    UserEntity userEntity = UserEntityFaker.getFake();
    when(userRepositoryMock.findFirstByUsernameEquals(any(String.class)))
        .thenReturn(Optional.of(userEntity));

    UserEntity user = userService.searchLocalUserByUsername(userEntity.getUsername());

    assertNotNull(user);
    assertEquals(userEntity.getUsername(), user.getUsername());

    verify(userRepositoryMock).findFirstByUsernameEquals(any(String.class));
  }

  @Test
  void searchLocalUserByUsernameNotFound() {
    when(userRepositoryMock.findFirstByUsernameEquals(any(String.class)))
        .thenReturn(Optional.empty());

    try {
      UserEntity user = userService.searchLocalUserByUsername("");
      assertNull(user);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(userRepositoryMock).findFirstByUsernameEquals(any(String.class));
  }

  @Test
  void createUserAlreadyExistsError() {
    Response response = mock(Response.class);
    when(response.getStatus()).thenReturn(409);
    when(usersResourceMock.create(any(UserRepresentation.class))).thenReturn(response);

    try {
      userService.createUser(UpdateOrCreateUserDTO.builder().build());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceAlreadyExistsException.class, e);
    }

    verify(response).getStatus();
    verify(usersResourceMock).create(any(UserRepresentation.class));
  }

  @Test
  void banUser() {
    UserEntity userEntity = UserEntityFaker.getFake();
    when(userRepositoryMock.findFirstByUsernameEquals(any(String.class)))
        .thenReturn(Optional.of(userEntity));

    userService.banUser(userEntity.getUsername());

    verify(userRepositoryMock).findFirstByUsernameEquals(any(String.class));
  }

  @Test
  void quitBanUser() {
    UserEntity userEntity = UserEntityFaker.getFake();
    when(userRepositoryMock.findFirstByUsernameEquals(any(String.class)))
        .thenReturn(Optional.of(userEntity));

    userService.quitBanUser(userEntity.getUsername());

    verify(userRepositoryMock).findFirstByUsernameEquals(any(String.class));
  }
}
