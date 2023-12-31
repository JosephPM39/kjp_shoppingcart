package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import com.kjp.shoppingcart.entities.ReviewEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewEntityFaker {
  public static ReviewEntity getFake() {
    Faker faker = new Faker();
    ReviewEntity fake = new ReviewEntity();
    fake.setId(UUID.randomUUID());
    fake.setProductId(UUID.randomUUID());
    fake.setUserId(UUID.randomUUID());
    fake.setTitle(FakerUtils.truncateFromStart(faker.commerce().productName(), 40));
    fake.setDescription(FakerUtils.truncateFromStart(faker.commerce().material(), 255));
    fake.setCreatedAt(FakerUtils.getTimestamp());
    fake.setUpdatedAt(FakerUtils.getTimestamp());
    return fake;
  }

  public static List<ReviewEntity> getFakes(Integer quantity) {
    List<ReviewEntity> fakes = new ArrayList<>();
    for (int i = 0; i < quantity; i++) {
      fakes.add(getFake());
    }
    return fakes;
  }
}
