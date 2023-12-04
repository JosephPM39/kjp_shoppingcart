package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ReviewDTO;
import com.kjp.shoppingcart.dto.VotesDTO;
import com.kjp.shoppingcart.entities.ReviewEntity;
import java.util.List;
import java.util.UUID;

public interface IOpinionService {
  public void voteLikeProduct(UUID userId, UUID productId);

  public void voteDislikeProduct(UUID userId, UUID productId);

  public void removeProductVote(UUID userId, UUID productId);

  public VotesDTO getProductVotesCount(UUID productId);

  public void createReview(UUID userId, ReviewDTO reviewDTO);

  public void removeReview(UUID userId, UUID productId);

  public void updateReview(UUID userId, ReviewDTO reviewDTO);

  public List<ReviewDTO> getAllReviewsByUser(UUID userId);

  public ReviewDTO getProductReviewByUser(UUID userId, UUID productId);

  public List<ReviewEntity> getAllReviews();

  public List<ReviewEntity> getAllReviewForTheProduct(UUID productId);
}
