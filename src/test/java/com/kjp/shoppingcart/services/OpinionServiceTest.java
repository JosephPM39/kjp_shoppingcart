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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class OpinionServiceTest {

  @Autowired
  IOpinionService opinionService;
  @MockBean
  IVoteRepository voteRepository;
  @MockBean
  IReviewRepository reviewRepository;
  @MockBean
  IProductRepository productRepository;

  @Test
  void voteLikeProduct() {
    try {
      opinionService.voteLikeProduct(UUID.randomUUID(), UUID.randomUUID());
      assertFalse(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }
    verify(voteRepository).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void voteDislikeProduct() {
    VoteEntity vote = VoteEntityFaker.getFake();
    when(voteRepository.findByUserIdAndProductId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(vote));

    opinionService.voteDislikeProduct(vote.getUserId(), vote.getProductId());

    verify(voteRepository).save(any(VoteEntity.class));
    verify(voteRepository).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductVote() {
    VoteEntity vote = VoteEntityFaker.getFake();
    when(voteRepository.findByUserIdAndProductId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(vote));

    opinionService.removeProductVote(vote.getUserId(), vote.getProductId());

    verify(voteRepository).deleteById(any(UUID.class));
    verify(voteRepository).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void removeProductVoteNotFound() {
    when(voteRepository.findByUserIdAndProductId(any(UUID.class), any(UUID.class))).thenReturn(Optional.empty());

    try {
      opinionService.removeProductVote(UUID.randomUUID(), UUID.randomUUID());
      assertNull(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceNotFoundException.class, e);
    }

    verify(voteRepository).findByUserIdAndProductId(any(UUID.class), any(UUID.class));
  }

  @Test
  void getProductVotesCount() {
    when(voteRepository.countVoteEntitiesByProductIdEqualsAndLikedEquals(any(UUID.class), eq(true))).thenReturn(10);
    when(voteRepository.countVoteEntitiesByProductIdEqualsAndLikedEquals(any(UUID.class), eq(false))).thenReturn(5);

    VotesDTO votes = opinionService.getProductVotesCount(UUID.randomUUID());

    assertEquals(10, votes.likes());
    assertEquals(5, votes.dislikes());

    verify(voteRepository).countVoteEntitiesByProductIdEqualsAndLikedEquals(any(UUID.class), eq(true));
    verify(voteRepository).countVoteEntitiesByProductIdEqualsAndLikedEquals(any(UUID.class), eq(false));
  }

  @Test
  void createReview() {
    when(productRepository.existsById(any(UUID.class))).thenReturn(true);
    when(reviewRepository.existsByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class))).thenReturn(false);
    ReviewDTO reviewDTO = ReviewDTOFaker.getFake();

    opinionService.createReview(UUID.randomUUID(), reviewDTO);

    verify(reviewRepository).existsByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
    verify(reviewRepository).save(any(ReviewEntity.class));
    verify(productRepository).existsById(any(UUID.class));
  }


  @Test
  void createReviewAlreadyExistException() {
    when(productRepository.existsById(any(UUID.class))).thenReturn(true);
    when(reviewRepository.existsByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class))).thenReturn(true);
    ReviewDTO reviewDTO = ReviewDTOFaker.getFake();
    try {
      opinionService.createReview(UUID.randomUUID(), reviewDTO);
      assertFalse(true);
    } catch (Exception e) {
      assertInstanceOf(ResourceAlreadyExistsException.class, e);
    }

    verify(reviewRepository).existsByUserIdEqualsAndProductIdEquals(any(UUID.class), any(UUID.class));
    verify(productRepository).existsById(any(UUID.class));
  }

  @Test
  void removeReview() {}

  @Test
  void updateReview() {}

  @Test
  void getAllReviews() {}

  @Test
  void getAllReviewForTheProduct() {}

  @Test
  void getAllReviewsByUser() {}

  @Test
  void getProductReviewByUser() {}
}
