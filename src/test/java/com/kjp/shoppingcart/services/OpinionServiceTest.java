package com.kjp.shoppingcart.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kjp.shoppingcart.dto.ReviewDTO;
import com.kjp.shoppingcart.dto.VotesDTO;
import com.kjp.shoppingcart.entities.ReviewEntity;
import com.kjp.shoppingcart.entities.VoteEntity;
import com.kjp.shoppingcart.exceptions.ResourceAlreadyExistsException;
import com.kjp.shoppingcart.exceptions.ResourceNotFoundException;
import com.kjp.shoppingcart.fakers.ReviewDTOFaker;
import com.kjp.shoppingcart.fakers.ReviewEntityFaker;
import com.kjp.shoppingcart.fakers.VoteEntityFaker;
import com.kjp.shoppingcart.repositories.IProductRepository;
import com.kjp.shoppingcart.repositories.IReviewRepository;
import com.kjp.shoppingcart.repositories.IVoteRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OpinionServiceTest {

  @Autowired IOpinionService opinionService;
  @MockBean IVoteRepository voteRepositoryMock;
  @MockBean IReviewRepository reviewRepositoryMock;
  @MockBean IProductRepository productRepositoryMock;

  @Test
  void voteLikeProduct() {
    VoteEntity vote = VoteEntityFaker.getFake();
    when(voteRepositoryMock.findByUserIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(vote));

    opinionService.voteLikeProduct(vote.getUserId(), vote.getProductId());

    verify(voteRepositoryMock).save(any(VoteEntity.class));
    verify(voteRepositoryMock).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void voteLikeProductNotFound() {
    try {
      opinionService.voteLikeProduct(UUID.randomUUID(), UUID.randomUUID());
      assertFalse(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }
    verify(voteRepositoryMock).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void voteDislikeProduct() {
    VoteEntity vote = VoteEntityFaker.getFake();
    when(voteRepositoryMock.findByUserIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(vote));

    opinionService.voteDislikeProduct(vote.getUserId(), vote.getProductId());

    verify(voteRepositoryMock).save(any(VoteEntity.class));
    verify(voteRepositoryMock).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductVote() {
    VoteEntity vote = VoteEntityFaker.getFake();
    when(voteRepositoryMock.findByUserIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(vote));

    opinionService.removeProductVote(vote.getUserId(), vote.getProductId());

    verify(voteRepositoryMock).deleteById(any(UUID.class));
    verify(voteRepositoryMock).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductVoteNotFound() {
    when(voteRepositoryMock.findByUserIdAndProductId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      opinionService.removeProductVote(UUID.randomUUID(), UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(voteRepositoryMock).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void getProductVotesCount() {
    when(voteRepositoryMock.countVoteEntitiesByProductIdEqualsAndLikedEquals(
            any(UUID.class), eq(true)))
        .thenReturn(10);
    when(voteRepositoryMock.countVoteEntitiesByProductIdEqualsAndLikedEquals(
            any(UUID.class), eq(false)))
        .thenReturn(5);

    VotesDTO votes = opinionService.getProductVotesCount(UUID.randomUUID());

    assertEquals(10, votes.likes());
    assertEquals(5, votes.dislikes());

    verify(voteRepositoryMock)
        .countVoteEntitiesByProductIdEqualsAndLikedEquals(any(UUID.class), eq(true));
    verify(voteRepositoryMock)
        .countVoteEntitiesByProductIdEqualsAndLikedEquals(any(UUID.class), eq(false));
  }

  @Test
  void createReview() {
    when(productRepositoryMock.existsById(any(UUID.class))).thenReturn(true);
    when(reviewRepositoryMock.existsByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(false);
    ReviewDTO reviewDTO = ReviewDTOFaker.getFake();

    opinionService.createReview(UUID.randomUUID(), reviewDTO);

    verify(reviewRepositoryMock)
        .existsByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
    verify(reviewRepositoryMock).save(any(ReviewEntity.class));
    verify(productRepositoryMock).existsById(any(UUID.class));
  }

  @Test
  void createReviewAlreadyExistException() {
    when(productRepositoryMock.existsById(any(UUID.class))).thenReturn(true);
    when(reviewRepositoryMock.existsByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(true);
    ReviewDTO reviewDTO = ReviewDTOFaker.getFake();
    try {
      opinionService.createReview(UUID.randomUUID(), reviewDTO);
      assertFalse(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceAlreadyExistsException.class, e);
    }

    verify(reviewRepositoryMock)
        .existsByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
    verify(productRepositoryMock).existsById(any(UUID.class));
  }

  @Test
  void removeReview() {
    ReviewEntity reviewEntity = ReviewEntityFaker.getFake();
    when(reviewRepositoryMock.findFirstByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(reviewEntity));

    this.opinionService.removeReview(reviewEntity.getUserId(), reviewEntity.getProductId());

    verify(reviewRepositoryMock)
        .findFirstByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
    verify(reviewRepositoryMock).deleteById(any(UUID.class));
  }

  @Test
  void removeReviewNotFound() {
    when(reviewRepositoryMock.findFirstByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      this.opinionService.removeReview(UUID.randomUUID(), UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(reviewRepositoryMock)
        .findFirstByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
  }

  @Test
  void updateReview() {
    ReviewDTO reviewDTO = ReviewDTOFaker.getFake();
    ReviewEntity reviewEntity = ReviewEntityFaker.getFake();
    when(reviewRepositoryMock.findFirstByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(reviewEntity));

    this.opinionService.updateReview(reviewEntity.getUserId(), reviewDTO);

    verify(reviewRepositoryMock)
        .findFirstByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
    verify(reviewRepositoryMock).save(any(ReviewEntity.class));
  }

  @Test
  void updateReviewNotFound() {
    ReviewDTO reviewDTO = ReviewDTOFaker.getFake();
    when(reviewRepositoryMock.findFirstByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      this.opinionService.updateReview(UUID.randomUUID(), reviewDTO);
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(reviewRepositoryMock)
        .findFirstByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
  }

  @Test
  void getAllReviews() {
    when(reviewRepositoryMock.findAll()).thenReturn(new ArrayList<>());

    List<ReviewEntity> reviewEntities = opinionService.getAllReviews();

    assertNotNull(reviewEntities);
    assertEquals(0, reviewEntities.size());

    verify(reviewRepositoryMock).findAll();
  }

  @Test
  void getAllReviewForTheProduct() {
    when(reviewRepositoryMock.findAllByProductIdEquals(any(UUID.class)))
        .thenReturn(new ArrayList<>());

    List<ReviewEntity> reviewEntities = opinionService.getAllReviewForTheProduct(UUID.randomUUID());

    assertNotNull(reviewEntities);
    assertEquals(0, reviewEntities.size());

    verify(reviewRepositoryMock).findAllByProductIdEquals(any(UUID.class));
  }

  @Test
  void getAllReviewsByUser() {
    when(reviewRepositoryMock.findAllByUserIdEquals(any(UUID.class))).thenReturn(new ArrayList<>());

    List<ReviewDTO> reviewEntities = opinionService.getAllReviewsByUser(UUID.randomUUID());

    assertNotNull(reviewEntities);
    assertEquals(0, reviewEntities.size());

    verify(reviewRepositoryMock).findAllByUserIdEquals(any(UUID.class));
  }

  @Test
  void getProductReviewByUser() {
    ReviewEntity reviewEntity = ReviewEntityFaker.getFake();
    when(reviewRepositoryMock.findFirstByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.of(reviewEntity));

    ReviewDTO reviewDTO =
        opinionService.getProductReviewByUser(
            reviewEntity.getUserId(), reviewEntity.getProductId());

    assertNotNull(reviewDTO);
    assertEquals(reviewEntity.getProductId(), reviewDTO.productId());

    verify(reviewRepositoryMock)
        .findFirstByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
  }

  @Test
  void getProductReviewByUserNotFound() {
    when(reviewRepositoryMock.findFirstByUserIdEqualsAndProductIdEquals(
            any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    try {
      ReviewDTO reviewDTO =
          opinionService.getProductReviewByUser(UUID.randomUUID(), UUID.randomUUID());
      assertNull(reviewDTO);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(reviewRepositoryMock)
        .findFirstByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
  }
}
