package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IVoteRepository extends IBaseRepository<VoteEntity, UUID> {
    public Optional<VoteEntity> findByUserIdAndProductId(UUID userId, UUID productId);

    public void deleteByUserIdAndProductId(UUID userId, UUID productId);

    public Integer countVoteEntitiesByProductIdEqualsAndLikedEquals(UUID productId, boolean liked);
}
