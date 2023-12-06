package com.kjp.shoppingcart.fakers;

import com.kjp.shoppingcart.entities.VoteEntity;
import java.util.UUID;

public class VoteEntityFaker {
  public static VoteEntity getFake() {
    VoteEntity vote = new VoteEntity();
    vote.setLiked(false);
    vote.setUserId(UUID.randomUUID());
    vote.setProductId(UUID.randomUUID());
    vote.setId(UUID.randomUUID());
    return vote;
  }
}
