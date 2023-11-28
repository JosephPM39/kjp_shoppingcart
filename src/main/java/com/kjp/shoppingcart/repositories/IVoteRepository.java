package com.kjp.shoppingcart.repositories;

import com.kjp.shoppingcart.entities.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IVoteRepository extends JpaRepository<VoteEntity, UUID> {
}
