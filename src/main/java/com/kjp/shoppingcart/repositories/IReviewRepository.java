package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IReviewRepository extends JpaRepository<ReviewEntity, UUID> {
}
