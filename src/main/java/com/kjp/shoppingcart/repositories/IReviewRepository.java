package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReviewRepository extends IBaseRepository<ReviewEntity, UUID> {
    public Optional<ReviewEntity> findFirstByUserIdEqualsAndProductIdEquals(UUID userId, UUID productId);

    public boolean existsByUserIdEqualsAndProductIdEquals(UUID userId, UUID productId);

    public void deleteByUserIdEqualsAndProductIdEquals(UUID userId, UUID productId);

    public List<ReviewEntity> findAllByUserIdEquals(UUID userId);
}
