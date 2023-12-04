package com.kjp.shoppingcart.services;

import com.kjp.shoppingcart.dto.ReviewDTO;
import com.kjp.shoppingcart.dto.VotesDTO;
import com.kjp.shoppingcart.entities.ReviewEntity;
import com.kjp.shoppingcart.entities.VoteEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.mappers.ReviewMapper;
import com.kjp.shoppingcart.repositories.IProductRepository;
import com.kjp.shoppingcart.repositories.IReviewRepository;
import com.kjp.shoppingcart.repositories.IVoteRepository;
import com.kjp.shoppingcart.utils.ProductServiceUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpinionService implements IOpinionService {

  IVoteRepository voteRepository;
  IReviewRepository reviewRepository;
  IProductRepository productRepository;

  @Autowired
  public OpinionService(
      IVoteRepository voteRepository,
      IReviewRepository reviewRepository,
      IProductRepository productRepository) {
    this.voteRepository = voteRepository;
    this.reviewRepository = reviewRepository;
    this.productRepository = productRepository;
  }

  private VoteEntity getExistingVoteOrNewInstance(UUID userId, UUID productId) {
    Optional<VoteEntity> optionalVote =
        this.voteRepository.findByUserIdAndProductId(userId, productId);
    if (optionalVote.isPresent()) {
      return optionalVote.get();
    }
    ProductServiceUtils.throwIfProductNotFound(productId, productRepository);
    VoteEntity vote = new VoteEntity();
    vote.setUserId(userId);
    vote.setProductId(productId);
    return vote;
  }

  @Override
  public void voteLikeProduct(UUID userId, UUID productId) {
    VoteEntity vote = getExistingVoteOrNewInstance(userId, productId);
    vote.setLiked(true);
    this.voteRepository.save(vote);
  }

  @Override
  public void voteDislikeProduct(UUID userId, UUID productId) {
    VoteEntity vote = getExistingVoteOrNewInstance(userId, productId);
    vote.setLiked(false);
    this.voteRepository.save(vote);
  }

  @Override
  public void removeProductVote(UUID userId, UUID productId) {
    this.voteRepository.deleteByUserIdAndProductId(userId, productId);
  }

  @Override
  public VotesDTO getProductVotesCount(UUID productId) {
    Integer likes =
        this.voteRepository.countVoteEntitiesByProductIdEqualsAndLikedEquals(productId, true);
    Integer dislikes =
        this.voteRepository.countVoteEntitiesByProductIdEqualsAndLikedEquals(productId, false);
    return new VotesDTO(productId, likes, dislikes);
  }

  @Override
  public void createReview(UUID userId, ReviewDTO reviewDTO) {
    ProductServiceUtils.throwIfProductNotFound(reviewDTO.productId(), productRepository);
    boolean alreadyExists =
        reviewRepository.existsByUserIdEqualsAndProductIdEquals(userId, reviewDTO.productId());
    if (alreadyExists) {
      throw new ResourceAlreadyExistsException(
          "The actual user already has a review for the product with the Id: "
              .concat(reviewDTO.productId().toString()));
    }

    ReviewEntity review = new ReviewEntity();

    review.setDescription(reviewDTO.description());
    review.setProductId(reviewDTO.productId());
    review.setUserId(userId);
    review.setTitle(reviewDTO.title());

    this.reviewRepository.save(review);
  }

  @Override
  public void removeReview(UUID userId, UUID productId) {
    this.reviewRepository.deleteByUserIdEqualsAndProductIdEquals(userId, productId);
  }

  @Override
  public void updateReview(UUID userId, ReviewDTO reviewDTO) {}

  @Override
  public List<ReviewEntity> getAllReviews() {
    return this.reviewRepository.findAll();
  }

  @Override
  public List<ReviewEntity> getAllReviewForTheProduct(UUID productId) {
    return this.reviewRepository.findAllByProductIdEquals(productId);
  }

  @Override
  public List<ReviewDTO> getAllReviewsByUser(UUID userId) {
    List<ReviewEntity> reviewEntities = this.reviewRepository.findAllByUserIdEquals(userId);
    return ReviewMapper.getReviewDTOList(reviewEntities);
  }

  @Override
  public ReviewDTO getProductReviewByUser(UUID userId, UUID productId) {
    return ReviewMapper.getReviewDTO(getReviewEntity(userId, productId));
  }

  private ReviewEntity getReviewEntity(UUID userId, UUID productId) {
    Optional<ReviewEntity> optionalReviewEntity =
        this.reviewRepository.findFirstByUserIdEqualsAndProductIdEquals(userId, productId);

    if (optionalReviewEntity.isPresent()) {
      return optionalReviewEntity.get();
    }

    throw new ResourceNotFoundException(
        "The actual user don't has review for the product with Id: "
            .concat(productId.toString())
            .concat(" Already exists"));
  }
}
