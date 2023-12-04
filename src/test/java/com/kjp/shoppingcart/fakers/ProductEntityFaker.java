package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import com.kjp.shoppingcart.entities.BrandEntity;
import com.kjp.shoppingcart.entities.ProductEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductEntityFaker {
    public static ProductEntity getFake(List<ProductEntity> list) {
        Faker faker = new Faker();
        ProductEntity fake = new ProductEntity();
        fake.setId(UUID.randomUUID());
        fake.setName(FakerUtils.toUniqueAndLimitSize(faker.commerce().productName(), 40));
        fake.setDescription(FakerUtils.truncateFromStart(faker.commerce().material(), 255));
        fake.setCode(FakerUtils.truncateFromStart(faker.commerce().promotionCode(), 255));
        fake.setPrice(new BigDecimal("100.02"));
        fake.setDisabled(faker.random().nextBoolean());
        fake.setBrandId(FakerUtils.randomIdFromEntityList(list));
        fake.setCreatedAt(FakerUtils.getTimestamp());
        fake.setUpdatedAt(FakerUtils.getTimestamp());
        return fake;
    }

    public static List<ProductEntity> getFakes(List<ProductEntity> list,  Integer quantity) {
        List<ProductEntity> fakes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            fakes.add(getFake(list));
        }
        return fakes;
    }
}
