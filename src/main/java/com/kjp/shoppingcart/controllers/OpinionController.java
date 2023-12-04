package com.kjp.shoppingcart.controllers;

import com.kjp.shoppingcart.dto.ReviewDTO;
import com.kjp.shoppingcart.dto.VotesDTO;
import com.kjp.shoppingcart.entities.ReviewEntity;
import com.kjp.shoppingcart.services.IOpinionService;
import com.kjp.shoppingcart.services.IUserService;
import java.util.List;
import java.util.UUID;

import com.kjp.shoppingcart.validations.groups.CreateGroup;
import com.kjp.shoppingcart.validations.groups.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/opinions")
public class OpinionController {

  private final IOpinionService opinionService;
  private final IUserService userService;

  @Autowired
  public OpinionController(IOpinionService opinionService, IUserService userService) {
    this.opinionService = opinionService;
    this.userService = userService;
  }

  @PostMapping("/product-vote/{productId}/like")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void voteLikeProduct(@PathVariable("productId") UUID productId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.opinionService.voteLikeProduct(userId, productId);
  }

  @PostMapping("/product-vote/{productId}/dislike")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void voteDislikeProduct(@PathVariable("productId") UUID productId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.opinionService.voteDislikeProduct(userId, productId);
  }

  @DeleteMapping("/product-vote/{productId}")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void removeProductVote(@PathVariable("productId") UUID productId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.opinionService.removeProductVote(userId, productId);
  }

  @GetMapping("/product-vote/{productId}")
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public VotesDTO getProductVotesCount(@PathVariable("productId") UUID productId) {
    return this.opinionService.getProductVotesCount(productId);
  }

  @GetMapping("/all-product-review/{productId}")
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public List<ReviewEntity> getAllReviewsForTheProduct(@PathVariable("productId") UUID productId) {
    return this.opinionService.getAllReviewForTheProduct(productId);
  }

  @GetMapping("/all-product-review")
  @PreAuthorize(
      "hasAnyRole(@environment.getProperty('app.auth.role-admin'), @environment.getProperty('app.auth.role-user'))")
  public List<ReviewEntity> getAllReviews() {
    return this.opinionService.getAllReviews();
  }

  @GetMapping("/product-review/{productId}")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public ReviewDTO getProductReviewByUser(@PathVariable("productId") UUID productId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    return this.opinionService.getProductReviewByUser(userId, productId);
  }

  @GetMapping("/product-review")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public List<ReviewDTO> getAllReviewsByUser() {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    return this.opinionService.getAllReviewsByUser(userId);
  }

  @PostMapping("/product-review")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void createReview(@Validated(CreateGroup.class) @RequestBody ReviewDTO reviewDTO) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.opinionService.createReview(userId, reviewDTO);
  }

  @PatchMapping("/product-review")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void updateReview(@Validated(UpdateGroup.class) @RequestBody ReviewDTO reviewDTO) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.opinionService.updateReview(userId, reviewDTO);
  }

  @DeleteMapping("/product-review/{productId}")
  @PreAuthorize("hasRole(@environment.getProperty('app.auth.role-user'))")
  public void removeReview(@PathVariable("productId") UUID productId) {
    UUID userId = this.userService.getAuthenticatedLocalUserId();
    this.opinionService.removeReview(userId, productId);
  }
}
