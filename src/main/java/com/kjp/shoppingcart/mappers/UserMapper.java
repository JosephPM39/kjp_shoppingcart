package com.kjp.shoppingcart.mappers;

import com.kjp.shoppingcart.dto.GetUserDTO;
import com.kjp.shoppingcart.entities.UserEntity;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserMapper {
    public static GetUserDTO toGetUserDTO(UserEntity user, Set<String> roles) {
        return GetUserDTO.builder()
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .username(user.getUsername())
            .roles(roles)
            .build();
    }

    public static GetUserDTO toGetUserDTO(UserRepresentation user) {
        return GetUserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .roles(Set.copyOf(user.getClientRoles().keySet()))
                .build();
    }

    public static List<GetUserDTO> toGetUserDTO(List<UserRepresentation> users) {
        List<GetUserDTO> getUserDTOList = new ArrayList<>();
        for (UserRepresentation user: users) {
            getUserDTOList.add(toGetUserDTO(user));
        }
        return getUserDTOList;
    }

    public static UserEntity toUserEntity(GetUserDTO userDTO, UUID userKeycloakId, boolean banned) {
        UserEntity user = new UserEntity();
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setUsername(userDTO.username());
        user.setBanned(banned);
        user.setKeycloakId(userKeycloakId);
        return user;
    }

    public static UserEntity toUserEntity(UserRepresentation userRepresentation, boolean banned) {
        UserEntity user = new UserEntity();
        user.setKeycloakId(UUID.fromString(userRepresentation.getId()));
        user.setBanned(banned);
        user.setUsername(userRepresentation.getUsername());
        user.setLastName(userRepresentation.getLastName());
        user.setFirstName(userRepresentation.getFirstName());
        user.setEmail(userRepresentation.getEmail());
        return user;
    }

}
