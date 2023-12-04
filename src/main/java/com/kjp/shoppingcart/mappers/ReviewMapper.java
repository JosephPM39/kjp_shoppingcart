package com.kjp.shoppingcart.mappers;

import com.kjp.shoppingcart.dto.ReviewDTO;
import com.kjp.shoppingcart.entities.ReviewEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewMapper {
  public static ReviewDTO getReviewDTO(ReviewEntity reviewEntity) {
    return ReviewDTO.builder()
        .description(reviewEntity.getDescription())
        .productId(reviewEntity.getProductId())
        .title(reviewEntity.getTitle())
        .build();
  }

  public static ReviewEntity getReviewEntity(UUID userId, ReviewDTO reviewDTO) {
    ReviewEntity review = new ReviewEntity();
    review.setTitle(reviewDTO.title());
    review.setUserId(userId);
    review.setDescription(reviewDTO.description());
    review.setProductId(reviewDTO.productId());
    return review;
  }

  public static List<ReviewDTO> getReviewDTOList(List<ReviewEntity> reviewEntityList) {
    List<ReviewDTO> reviewDTOList = new ArrayList<>();
    for (ReviewEntity review : reviewEntityList) {
      reviewDTOList.add(getReviewDTO(review));
    }
    return reviewDTOList;
  }
}
