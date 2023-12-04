package com.kjp.shoppingcart.fakers;

import com.github.javafaker.Faker;
import com.kjp.shoppingcart.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserEntityFaker {
    public static UserEntity getFake() {
        Faker faker = new Faker();
        UserEntity fake = new UserEntity();
        fake.setId(UUID.randomUUID());
        fake.setKeycloakId(UUID.randomUUID());
        fake.setUsername(FakerUtils.toUniqueAndLimitSize(faker.name().username(), 40));
        fake.setLastName(FakerUtils.toUniqueAndLimitSize(faker.name().lastName(), 40));
        fake.setFirstName(FakerUtils.toUniqueAndLimitSize(faker.name().firstName(), 40));
        fake.setBanned(faker.random().nextBoolean());
        fake.setCreatedAt(FakerUtils.getTimestamp());
        fake.setUpdatedAt(FakerUtils.getTimestamp());
        return fake;
    }

    public static List<UserEntity> getFakes(Integer quantity) {
        List<UserEntity> fakes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            fakes.add(getFake());
        }
        return fakes;
    }
}

