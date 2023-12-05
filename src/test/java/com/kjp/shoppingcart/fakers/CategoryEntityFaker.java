package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import com.kjp.shoppingcart.entities.CategoryEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryEntityFaker {
  public static CategoryEntity getFake() {
    Faker faker = new Faker();
    CategoryEntity fake = new CategoryEntity();
    fake.setId(UUID.randomUUID());
    fake.setName(FakerUtils.toUniqueAndLimitSize(faker.commerce().department(), 40));
    fake.setDisabled(faker.random().nextBoolean());
    fake.setDescription(FakerUtils.truncateFromStart(faker.commerce().material(), 255));
    fake.setCreatedAt(FakerUtils.getTimestamp());
    fake.setUpdatedAt(FakerUtils.getTimestamp());
    return fake;
  }

  public static List<CategoryEntity> getFakes(Integer quantity) {
    List<CategoryEntity> fakes = new ArrayList<>();
    for (int i = 0; i < quantity; i++) {
      fakes.add(getFake());
    }
    return fakes;
  }
}
