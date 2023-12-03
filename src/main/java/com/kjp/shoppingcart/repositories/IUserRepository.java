package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends IBaseRepository<UserEntity, UUID> {
    public Optional<UserEntity> findFirstByKeycloakIdEquals(UUID keycloakId);
}
