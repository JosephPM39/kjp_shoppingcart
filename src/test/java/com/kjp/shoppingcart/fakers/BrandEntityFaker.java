package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import com.kjp.shoppingcart.entities.BrandEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BrandEntityFaker {
    public static BrandEntity getFake() {
        Faker faker = new Faker();
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(UUID.randomUUID());
        brandEntity.setName(FakerUtils.toUniqueAndLimitSize(faker.commerce().productName(), 40));
        brandEntity.setCreatedAt(FakerUtils.getTimestamp());
        brandEntity.setUpdatedAt(FakerUtils.getTimestamp());
        return brandEntity;
    }

    public static List<BrandEntity> getFakes(Integer quantity) {
        List<BrandEntity> brandEntities = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            brandEntities.add(getFake());
        }
        return brandEntities;
    }
}
